/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.dto.constraints;

import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import org.junit.Assert;
import org.junit.BeforeClass;

/**
 *
 * @author tarasev
 */
public class BaseConstraintAnnotationsTest {

    protected static final String EMPTY_STRING = "";
    
    protected static Validator validator;

    @BeforeClass
    public static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    protected void testInvalidObject(Object o) {
        Set<ConstraintViolation<Object>> violations = validator.validate(o);
        Assert.assertFalse("Expected constraint violation", violations.isEmpty());
    }

    protected void testValidObject(Object o) {
        Set<ConstraintViolation<Object>> violations = validator.validate(o);
        Assert.assertTrue("Constraint violation not expected", violations.isEmpty());
    }
    
    
    protected void testInvalidObject(ObjectHolder oh) {
        Set<ConstraintViolation<Object>> violations = validator.validate(oh.getObjectHolder());
        Assert.assertFalse("Expected constraint violation", violations.isEmpty());
    }

    protected void testValidObject(ObjectHolder oh) {
        Set<ConstraintViolation<Object>> violations = validator.validate(oh.getObjectHolder());
        Assert.assertTrue("Constraint violation not expected", violations.isEmpty());
    }
}
