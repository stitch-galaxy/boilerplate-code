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
public class CanvasConstraintAnnotationsTest extends BaseConstraintAnnotationsTest {

    private static class CanvasWrapper implements ObjectHolder {

        @Canvas
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

    private static class CanvasRequiredWrapper implements ObjectHolder {

        @CanvasRequired
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

    
    private static final String VALID_CANVAS_CODE = "Aida 14";

    @Test
    public void testConstraint() {
        CanvasWrapper o = new CanvasWrapper();
        testConstraintImpl(o);
        o.setObject(null);
        testValidObject(o);
    }
    
    @Test
    public void testRequiredConstraint() {
        CanvasRequiredWrapper o = new CanvasRequiredWrapper();
        testConstraintImpl(o);
        o.setObject(null);
        testInvalidObject(o);
    }

    private void testConstraintImpl(ObjectHolder o) {
        o.setObject(EMPTY_STRING);
        testInvalidObject(o);
        o.setObject(VALID_CANVAS_CODE);
        testValidObject(o);
    }
}
