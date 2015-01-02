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
public class UserFirstNameAnnotationsTest extends BaseConstraintAnnotationsTest {

    private static class UserFirstnameWrapper implements ObjectHolder {

        @UserFirstName
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

    private static class UserFirstnameRequiredWrapper implements ObjectHolder {

        @UserFirstNameRequired
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

    
    private static final String VALID_USER_FIRST_NAME_1 = "У";

    @Test
    public void testConstraint() {
        UserFirstnameWrapper o = new UserFirstnameWrapper();
        testConstraintImpl(o);
        o.setObject(null);
        testValidObject(o);
    }
    
    @Test
    public void testRequiredConstraint() {
        UserFirstnameRequiredWrapper o = new UserFirstnameRequiredWrapper();
        testConstraintImpl(o);
        o.setObject(null);
        testInvalidObject(o);
    }

    private void testConstraintImpl(ObjectHolder o) {
        o.setObject(EMPTY_STRING);
        testInvalidObject(o);
        o.setObject(VALID_USER_FIRST_NAME_1);
        testValidObject(o);
    }
}
