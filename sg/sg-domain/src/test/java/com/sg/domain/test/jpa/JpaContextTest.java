package com.sg.domain.test.jpa;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.sg.domain.service.SgService;
import com.sg.domain.spring.configuration.JpaContext;
import com.sg.domain.spring.configuration.JpaServiceContext;
import com.sg.domain.spring.configuration.MapperContext;
import com.sg.domain.spring.configuration.SgCryptoContext;
import com.sg.domain.spring.configuration.SgMailServiceContext;
import com.sg.domain.spring.configuration.ValidatorContext;
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
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = {SgMailServiceContext.class, SgCryptoContext.class, ValidatorContext.class, JpaContext.class, MapperContext.class, JpaServiceContext.class})
public class JpaContextTest {
    
    @Autowired
    SgService service;
          
    
    public JpaContextTest() {
    }
    
    @Test
    public void emptyTest(){
    }
}
