package com.sg.domain.test.jpa;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.sg.domain.service.SgService;
import com.sg.domain.service.jpa.spring.PersistenceContextConfig;
import com.sg.domain.service.jpa.spring.ServiceContextConfig;
import com.sg.domain.service.jpa.spring.ValidatorContextConfig;
import com.sg.domain.test.spring.configuration.TestJpaServicePropertiesContextConfiguration;
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
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = {TestJpaServicePropertiesContextConfiguration.class, ValidatorContextConfig.class, PersistenceContextConfig.class, ServiceContextConfig.class})
public class JpaContextTest {
    
    @Autowired
    SgService service;
    
    public JpaContextTest() {
    }
    
    @Test
    public void contextTest(){
    }
}
