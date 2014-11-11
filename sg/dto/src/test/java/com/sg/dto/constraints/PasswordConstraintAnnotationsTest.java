/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.dto.constraints;

import java.math.BigDecimal;
import org.junit.Test;

/**
 *
 * @author tarasev
 */
public class PasswordConstraintAnnotationsTest extends BaseConstraintAnnotationsTest {

    
    private static class PasswordRequiredWrapper implements ObjectHolder {

        @PasswordRequired
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

    
    @Test
    public void testRequiredConstraint() {
        PasswordRequiredWrapper o = new PasswordRequiredWrapper();
        testConstraintImpl(o);
        o.setObject(null);
        testInvalidObject(o);
    }
    
    private static final String VALID_PASSWORD_1 = "a";

    private void testConstraintImpl(ObjectHolder o) {
        o.setObject(EMPTY_STRING);
        testInvalidObject(o);
        o.setObject(VALID_PASSWORD_1);
        testValidObject(o);
    }
}
