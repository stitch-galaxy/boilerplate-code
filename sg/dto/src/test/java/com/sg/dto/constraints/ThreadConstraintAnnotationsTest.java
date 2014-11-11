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
public class ThreadConstraintAnnotationsTest extends BaseConstraintAnnotationsTest {

    private static class ThreadWrapper implements ObjectHolder {

        @Thread
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

    private static class ThreadRequiredWrapper implements ObjectHolder {

        @ThreadRequired
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

    
    private static final String VALID_THREAD_CODE = "DMC";

    @Test
    public void testConstraint() {
        ThreadWrapper o = new ThreadWrapper();
        testConstraintImpl(o);
        o.setObject(null);
        testValidObject(o);
    }
    
    @Test
    public void testRequiredConstraint() {
        ThreadRequiredWrapper o = new ThreadRequiredWrapper();
        testConstraintImpl(o);
        o.setObject(null);
        testInvalidObject(o);
    }

    private void testConstraintImpl(ObjectHolder o) {
        o.setObject(EMPTY_STRING);
        testInvalidObject(o);
        o.setObject(VALID_THREAD_CODE);
        testValidObject(o);
    }
}
