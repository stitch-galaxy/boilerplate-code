/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.dto.request;

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
public class BaseDtoConstraintsTest {

    protected static final String EMPTY_STRING = "";

    protected static Validator validator;

    @BeforeClass
    public static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    protected void testConstraintViolations(Object dto, Set<String> messages) {
        Set<ConstraintViolation<Object>> violations = validator.validate(dto);
        Assert.assertEquals(messages.size(), violations.size());
        for (ConstraintViolation<Object> violation : violations) {
            Assert.assertTrue(messages.contains(violation.getMessage()));
        }
    }
}
