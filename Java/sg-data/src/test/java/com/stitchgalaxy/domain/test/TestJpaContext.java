package com.stitchgalaxy.domain.test;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.stitchgalaxy.domain.service.SitchGalaxyService;
import com.stitchgalaxy.domain.spring.configuration.JpaConfig;
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
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = JpaConfig.class)
public class TestJpaContext {
    
    @Autowired
    SitchGalaxyService service;
            
    public TestJpaContext() {
    }
    
    @Test
    public void testCase()
    {
        service.addProduct();
    }
}
