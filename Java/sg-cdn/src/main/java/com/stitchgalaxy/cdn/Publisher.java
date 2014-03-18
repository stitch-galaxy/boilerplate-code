/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.stitchgalaxy.cdn;

import com.google.common.io.Closeables;
import org.jclouds.ContextBuilder;
import org.jclouds.cloudfiles.CloudFilesClient;
import org.jclouds.openstack.swift.domain.SwiftObject;

import java.io.*;
import java.net.URI;

import com.stitchgalaxy.cdn.Constants;

public class Publisher implements Closeable {
   private final CloudFilesClient cloudFilesClient;

   
   public static void main(String[] args) throws IOException {
      Publisher cloudFilesPublish = new Publisher("stitchgalaxy", "bc8d9ee822ca4ea5b986de0e146fa803");

      try {
         cloudFilesPublish.createContainer();
         cloudFilesPublish.createObjectFromFile();
         cloudFilesPublish.enableCdnContainer();
      }
      catch (IOException e) {
         e.printStackTrace();
      }
      finally {
         cloudFilesPublish.close();
      }
   }

   public Publisher(String username, String apiKey) {
      cloudFilesClient = ContextBuilder.newBuilder(Constants.PROVIDER)
            .credentials(username, apiKey)
            .buildApi(CloudFilesClient.class);
   }

   /**
    * This method will create a container in Cloud Files where you can store and
    * retrieve any kind of digital asset.
    */
   private void createContainer() {
      System.out.format("Create Container%n");

      cloudFilesClient.createContainer(Constants.CONTAINER_PUBLISH);

      System.out.format("  %s%n", Constants.CONTAINER_PUBLISH);
   }

   /**
    * This method will put a plain text object into the container.
    */
   private void createObjectFromFile() throws IOException {
      System.out.format("Create Object From File%n");

      File tempFile = File.createTempFile(Constants.FILENAME, Constants.SUFFIX);
      tempFile.deleteOnExit();

      BufferedWriter out = new BufferedWriter(new FileWriter(tempFile));
      out.write("Hello Cloud Files");
      out.close();

      SwiftObject object = cloudFilesClient.newSwiftObject();
      object.getInfo().setName(Constants.FILENAME + Constants.SUFFIX);
      object.setPayload(tempFile);

      cloudFilesClient.putObject(Constants.CONTAINER_PUBLISH, object);

      System.out.format("  %s%s%n", Constants.FILENAME, Constants.SUFFIX);
   }

   /**
    * This method will put your container on a Content Distribution Network and
    * make it 100% publicly accessible over the Internet.
    */
   private void enableCdnContainer() {
      System.out.format("Enable CDN Container%n");

      URI cdnURI = cloudFilesClient.enableCDN(Constants.CONTAINER_PUBLISH);

      System.out.format("  Go to %s/%s%s%n", cdnURI, Constants.FILENAME, Constants.SUFFIX);
   }

   /**
    * Always close your service when you're done with it.
    */
   public void close() throws IOException {
      Closeables.close(cloudFilesClient, true);
   }
}
