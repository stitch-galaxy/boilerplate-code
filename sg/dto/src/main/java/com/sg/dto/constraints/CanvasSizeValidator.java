/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.dto.constraints;

import java.math.BigDecimal;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 *
 * @author tarasev
 */
public class CanvasSizeValidator implements  ConstraintValidator<CanvasSize, BigDecimal> {

    public static final BigDecimal MIN_STITCHES_PER_INCH = BigDecimal.ONE;
    public static final BigDecimal MAX_INCHES_PER_INCH = new BigDecimal(100);
    
    private CanvasSize constraintAnnotation;
    
    @Override
    public void initialize(CanvasSize constraintAnnotation) {
        this.constraintAnnotation = constraintAnnotation;
    }

    @Override
    public boolean isValid(BigDecimal o, ConstraintValidatorContext cvc) {
        if (o == null)
            return false;
        if (o.compareTo(MIN_STITCHES_PER_INCH) < 0)
        {
            return false;
        }
        if (o.compareTo(MAX_INCHES_PER_INCH) > 0)
        {
            return false;
        }
        return true;
    }
    
    
}
