/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.dto.constraints;

import org.junit.Test;

/**
 *
 * @author tarasev
 */
public class EmailConstraintAnnotationsTest extends BaseConstraintAnnotationsTest {

    private static class EmailWrapper implements ObjectHolder {

        @Email
        private String object;

        @Override
        public Object getObjectHolder() {
            return this;
        }

        @Override
        public void setObject(Object value) {
            this.object = (String) value;
        }
    }

    private static class EmailRequiredWrapper implements ObjectHolder {

        @EmailRequired
        private String object;

        @Override
        public Object getObjectHolder() {
            return this;
        }

        @Override
        public void setObject(Object value) {
            this.object = (String) value;
        }
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
    public void testConstraint() {
        EmailWrapper o = new EmailWrapper();
        testConstraintImpl(o);
        o.setObject(null);
        testValidObject(o);
    }

    @Test
    public void testRequiredConstraint() {
        EmailRequiredWrapper o = new EmailRequiredWrapper();
        testConstraintImpl(o);
        o.setObject(null);
        testInvalidObject(o);
    }

    private void testConstraintImpl(ObjectHolder o) {
        o.setObject(INVALID_EMAIL_1);
        testInvalidObject(o);
        o.setObject(INVALID_EMAIL_2);
        testInvalidObject(o);
        o.setObject(INVALID_EMAIL_3);
        testInvalidObject(o);
        o.setObject(INVALID_EMAIL_4);
        testInvalidObject(o);
        o.setObject(INVALID_EMAIL_5);
        testInvalidObject(o);
        o.setObject(INVALID_EMAIL_6);
        testInvalidObject(o);
        o.setObject(VALID_EMAIL_1);
        testValidObject(o);
        o.setObject(VALID_EMAIL_2);
        testValidObject(o);
    }
}
