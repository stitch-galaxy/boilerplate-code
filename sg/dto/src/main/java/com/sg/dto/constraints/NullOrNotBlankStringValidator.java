/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.dto.constraints;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 *
 * @author tarasev
 */
public class NullOrNotBlankStringValidator implements ConstraintValidator<NullOrNotBlankString, String> {

    private NullOrNotBlankString annotation;

    @Override
    public void initialize(NullOrNotBlankString annotation) {
        this.annotation = annotation;
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext cvc) {
        if (value == null) {
            return true;
        }
        return !value.trim().isEmpty();
    }

}
