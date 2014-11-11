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
public class UserLastnameAnnotationsTest extends BaseConstraintAnnotationsTest {

    private static class UserLastnameWrapper implements ObjectHolder {

        @UserLastname
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

    private static class UserLastnameRequiredWrapper implements ObjectHolder {

        @UserLastnameRequired
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

    
    private static final String VALID_USER_LAST_NAME_1 = "У";

    @Test
    public void testConstraint() {
        UserLastnameWrapper o = new UserLastnameWrapper();
        testConstraintImpl(o);
        o.setObject(null);
        testValidObject(o);
    }
    
    @Test
    public void testRequiredConstraint() {
        UserLastnameRequiredWrapper o = new UserLastnameRequiredWrapper();
        testConstraintImpl(o);
        o.setObject(null);
        testInvalidObject(o);
    }

    private void testConstraintImpl(ObjectHolder o) {
        o.setObject(EMPTY_STRING);
        testInvalidObject(o);
        o.setObject(VALID_USER_LAST_NAME_1);
        testValidObject(o);
    }
}
