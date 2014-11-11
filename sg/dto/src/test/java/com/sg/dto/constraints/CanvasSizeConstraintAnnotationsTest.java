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
public class CanvasSizeConstraintAnnotationsTest extends BaseConstraintAnnotationsTest {

    private static final BigDecimal VALID_CANVAS_STITCHES_PER_INCH = new BigDecimal(14);
    
    private static class CanvasSizeRequiredWrapper implements ObjectHolder {

        @CanvasSizeRequired
        private BigDecimal object;

        @Override
        public Object getObjectHolder() {
            return this;
        }

        @Override
        public void setObject(Object value) {
            this.object = (BigDecimal) value;
        }
    }

    @Test
    public void testRequiredConstraint() {
        CanvasSizeRequiredWrapper o = new CanvasSizeRequiredWrapper();
        testConstraintImpl(o);
        o.setObject(null);
        testInvalidObject(o);
    }

    private void testConstraintImpl(ObjectHolder o) {
        o.setObject(new BigDecimal(CanvasSizeRequired.MAX_STITCHES_PER_INCH).add(BigDecimal.ONE));
        testInvalidObject(o);
        o.setObject(new BigDecimal(CanvasSizeRequired.MIN_STITCHES_PER_INCH).subtract(BigDecimal.ONE));
        testInvalidObject(o);
        o.setObject(VALID_CANVAS_STITCHES_PER_INCH);
        testValidObject(o);
    }
}
