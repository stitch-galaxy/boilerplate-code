package com.sg.rest;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import com.sg.mail.service.EmailService;
import com.sg.rest.spring.JacksonMapperContext;
import com.sg.rest.spring.SgMailServiceContext;
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
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = {SgMailServiceContext.class, JacksonMapperContext.class})
public class SgMailServiceTest {

    @Autowired
    EmailService mailService;

    @Test
    public void testEmail() {
        //TODO: write good test
        mailService.sendVerificationEmail("token", "тарасов@вышивка.рф");
    }
    
}
