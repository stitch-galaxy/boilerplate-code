package com.sg.domain.test;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.sg.domain.service.SgService;
import com.sg.domain.spring.configuration.JpaConfig;
import com.sg.domain.spring.configuration.JpaServiceConfig;
import com.sg.domain.spring.configuration.MapperConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

/**
 *
 * @author tarasev
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = {JpaConfig.class, MapperConfig.class, JpaServiceConfig.class})
public class JpaContextTest {
    
    @Autowired
    SgService service;
          
    
    public JpaContextTest() {
    }
    
    @Test
    public void emptyTest(){
    }
}
