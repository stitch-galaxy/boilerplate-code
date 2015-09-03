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
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@SpringBootApplication(exclude = {
//    org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration.class
//})
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
