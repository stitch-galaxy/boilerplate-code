/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.rest.api;

/**
 *
 * @author Admin
 */
import com.sg.domain.spring.DomainConfig;
import com.sg.infrastructure.spring.InfrastructureConfig;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import org.apache.catalina.connector.Connector;
import org.apache.coyote.http11.Http11NioProtocol;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.FileCopyUtils;

@SpringBootApplication
@Import(value = {
    DomainConfig.class,
    InfrastructureConfig.class
})
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public EmbeddedServletContainerFactory servletContainer(
            @Value("${key.alias}") String keyAlias,
            @Value("${keystore.password}") String keystorePassword,
            @Value("${truststore.password}") String truststorePassword
    ) {
        TomcatEmbeddedServletContainerFactory tomcat = new TomcatEmbeddedServletContainerFactory();
        tomcat.addAdditionalTomcatConnectors(createSslConnector(keyAlias,
                keystorePassword,
                truststorePassword
        ));
        return tomcat;
    }

    private Connector createSslConnector(
            String keyAlias,
            String keystorePassword,
            String truststorePassword
    ) {
        Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
        Http11NioProtocol protocol = (Http11NioProtocol) connector.getProtocolHandler();
        try {
            File keystore = getKeyStoreFileFromDisk();//getKeyStoreFile();
            File truststore = keystore;
            connector.setScheme("https");
            connector.setSecure(true);
            connector.setPort(8443);
            protocol.setSSLEnabled(true);
            protocol.setKeystoreFile(keystore.getAbsolutePath());
            protocol.setKeystorePass(keystorePassword);
            protocol.setTruststoreFile(truststore.getAbsolutePath());
            protocol.setTruststorePass(truststorePassword);
            protocol.setKeyAlias(keyAlias);
            return connector;
        } catch (IOException ex) {
            throw new IllegalStateException("cant access keystore: [" + "keystore"
                    + "] or truststore: [" + "keystore" + "]", ex);
        }
    }

    private File getKeyStoreFileFromDisk() throws IOException {
        return new File("keystore");
    }

    private File getKeyStoreFile() throws IOException {
        ClassPathResource resource = new ClassPathResource("keystore");
        try {
            return resource.getFile();
        } catch (Exception ex) {
            File temp = File.createTempFile("keystore", ".tmp");
            FileCopyUtils.copy(resource.getInputStream(), new FileOutputStream(temp));
            return temp;
        }
    }

}
