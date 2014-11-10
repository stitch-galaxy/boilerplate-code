/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.domain.test.jpa;

import com.sg.domain.service.SgService;
import com.sg.domain.service.exception.SgDataValidationException;
import com.sg.domain.service.jpa.components.BusinessService;
import com.sg.domain.service.jpa.spring.PersistenceContextConfig;
import com.sg.domain.service.jpa.spring.ServiceContextConfig;
import com.sg.domain.service.jpa.spring.ValidatorContextConfig;
import com.sg.domain.test.spring.configuration.TestJpaServicePropertiesContextConfiguration;
import com.sg.dto.request.ThreadDeleteDto;
import javax.validation.ConstraintViolationException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 *
 * @author tarasev
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestJpaServicePropertiesContextConfiguration.class, PersistenceContextConfig.class, ValidatorContextConfig.class, ServiceContextConfig.class})
public class ValidationsTest {

    @Autowired
    private BusinessService service;

    @Autowired
    private SgService sgService;
    
    @Test
    public void testValidationsAsInExample() throws SgDataValidationException {
        try {
            sgService.convertToUpperCase("returnnull");
        } catch (ConstraintViolationException ex) {
            return;
        }
        
        fail("Was expecting a ConstraintViolationException.");
    }

    @Test
    public void testValidations() throws SgDataValidationException {        
        ThreadDeleteDto dto = new ThreadDeleteDto();
        try {
            sgService.delete(dto);
        } catch (ConstraintViolationException ex) {
            return;
        }
        fail("Was expecting a ConstraintViolationException.");
    }

    @Test
    public void testConvertToUpperCase() throws Exception {
        assertEquals("HELLO WORLD!", service.convertToUpperCase("hello world!"));
    }

    @Test
    public void testConvertToUpperCaseWithNullReturn() throws Exception {

        try {
            service.convertToUpperCase("returnnull");
        } catch (ConstraintViolationException ex) {
            return;
        }

        fail("Was expecting a ConstraintViolationException.");
    }

}
