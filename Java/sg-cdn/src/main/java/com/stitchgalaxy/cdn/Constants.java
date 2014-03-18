/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.stitchgalaxy.cdn;

/**
 *
 * @author tarasev
 */
public interface Constants {
   // The provider configures jclouds To use the Rackspace Cloud (US)
   // To use the Rackspace Cloud (UK) set the system property or default value to "cloudfiles-uk"
   public static final String PROVIDER = System.getProperty("provider.cf", "cloudfiles-us");
   public static final String ZONE = System.getProperty("zone", "IAD");

   public static final String CONTAINER_PUBLISH = "jclouds-example-publish";
   public static final String CONTAINER = "jclouds-example";
   public static final String FILENAME = "createObjectFromFile";
   public static final String SUFFIX = ".html";
}
