/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.dto.validations;

import com.sg.domain.service.exception.SgDataValidationException;
import com.sg.domain.service.jpa.spring.ValidatorContextConfig;
import com.sg.dto.constraints.CanvasCode;
import com.sg.dto.constraints.CanvasSize;
import com.sg.dto.constraints.CanvasSizeValidator;
import com.sg.dto.constraints.SgEmail;
import com.sg.dto.constraints.SgPassword;
import com.sg.dto.request.ThreadDeleteDto;
import java.math.BigDecimal;
import javax.validation.ConstraintViolationException;
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
@ContextConfiguration(classes = {ValidatorContextConfig.class, TestContextConfig.class})
public class ValidationsTest {

    @Autowired
    private TestValidatorComponent validator;

    public void testInvalidObject(Object o) {
        try {
            validator.validateObject(o);
            fail("Expected " + ConstraintViolationException.class.getName());
        } catch (ConstraintViolationException ex) {
        }
    }

    private void testValidObject(Object o) {
        validator.validateObject(o);
    }

    private static class CanvasCodeWrapper {

        @CanvasCode
        public String object;
    }

    private static final String EMPTY_STRING = "";
    private static final String VALID_CANVAS_CODE = "Aida 14";

    @Test
    public void testConstraintAnnotationCanvasCode() {
        CanvasCodeWrapper o = new CanvasCodeWrapper();
        o.object = null;
        testInvalidObject(o);
        o.object = EMPTY_STRING;
        testInvalidObject(o);
        o.object = VALID_CANVAS_CODE;
        testValidObject(o);
    }

    private static class CanvasSizeWrapper {

        @CanvasSize
        public BigDecimal object;
    }

    private static final BigDecimal VALID_CANVAS_STITCHES_PER_INCH = new BigDecimal(14);

    @Test
    public void testConstraintAnnotationCanvasSize() {
        CanvasSizeWrapper o = new CanvasSizeWrapper();
        o.object = BigDecimal.ZERO;
        testInvalidObject(o);
        o.object = null;
        testInvalidObject(o);
        o.object = CanvasSizeValidator.MAX_INCHES_PER_INCH.add(BigDecimal.ONE);
        testInvalidObject(o);
        o.object = CanvasSizeValidator.MIN_STITCHES_PER_INCH.subtract(BigDecimal.ONE);
        testInvalidObject(o);
        o.object = VALID_CANVAS_STITCHES_PER_INCH;
        testValidObject(o);
    }

    private static class SgEmailWrapper {

        @SgEmail
        public String object;
    }

    private static final String INVALID_EMAIL_1 = "abc@gmail.com ";
    private static final String INVALID_EMAIL_2 = " abc@gmail.com";
    private static final String INVALID_EMAIL_3 = "abc";
    private static final String INVALID_EMAIL_4 = "Тарасов@почта.рф ";
    private static final String INVALID_EMAIL_5 = " Тарасов@почта.рф";
    private static final String INVALID_EMAIL_6 = "тарасов";
    private static final String VALID_EMAIL_1 = "tarasov.e.a@gmail.com";
    private static final String VALID_EMAIL_2 = "тарасов@почта.рф";

    @Test
    public void testConstraintAnnotationSgEmail() {
        SgEmailWrapper o = new SgEmailWrapper();
        o.object = null;
        testInvalidObject(o);
        o.object = INVALID_EMAIL_1;
        testInvalidObject(o);
        o.object = INVALID_EMAIL_2;
        testInvalidObject(o);
        o.object = INVALID_EMAIL_3;
        testInvalidObject(o);
        o.object = INVALID_EMAIL_4;
        testInvalidObject(o);
        o.object = INVALID_EMAIL_5;
        testInvalidObject(o);
        o.object = INVALID_EMAIL_6;
        testInvalidObject(o);
        o.object = VALID_EMAIL_1;
        testValidObject(o);
        o.object = VALID_EMAIL_2;
        testValidObject(o);
    }

    private static class SgPasswordWrapper {

        @SgPassword
        public String object;
    }

    private static final String VALID_PASSWORD_1 = "a";

    @Test
    public void testConstraintAnnotationSgPassword() {
        SgPasswordWrapper o = new SgPasswordWrapper();
        o.object = null;
        testInvalidObject(o);
        o.object = EMPTY_STRING;
        testInvalidObject(o);
        o.object = VALID_PASSWORD_1;
        testValidObject(o);
    }

}
