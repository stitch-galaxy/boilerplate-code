package com.sg.domain.test.jpa;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import com.sg.domain.service.SgService;
import com.sg.domain.service.exception.SgDataValidationException;
import com.sg.domain.spring.configuration.JpaContext;
import com.sg.domain.spring.configuration.JpaServiceContext;
import com.sg.domain.spring.configuration.MapperContext;
import com.sg.domain.spring.configuration.ValidatorContext;
import com.sg.dto.SignupDto;
import java.util.Arrays;
import java.util.HashSet;
import junit.framework.Assert;
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
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = {ValidatorContext.class, JpaContext.class, MapperContext.class, JpaServiceContext.class})
public class JpaServiceDataValidationTest {

    @Autowired
    private SgService service;
    
    @Test
    public void testInvalidSignupDto()
    {
        SignupDto dto = new SignupDto();
        dto.setEmail("abc");
        try{
            service.signupUser(dto);
            Assert.fail("Expected " + SgDataValidationException.class.getName());
        }
        catch(SgDataValidationException e)
        {
            Assert.assertEquals(new HashSet<String>(Arrays.asList(new String[]{
                SignupDto.FIELD_SIGNUP_EMAIL,
                SignupDto.FIELD_SIGNUP_USER_BIRTH_DATE,
                SignupDto.FIELD_SIGNUP_USER_FIRST_NAME,
                SignupDto.FIELD_SIGNUP_USER_LAST_NAME,
            })),
                    e.getFieldErrors());
        }
    }
}
