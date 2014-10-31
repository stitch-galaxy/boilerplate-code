/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sg.domain.service.exception;

import java.util.HashSet;
import java.util.Set;
import javax.validation.ConstraintViolation;

/**
 *
 * @author tarasev
 */
public class SgDataValidationException extends Exception {

    private Set<String> fieldErrors;
    
    public SgDataValidationException(Set<ConstraintViolation<Object>> errors) {
        super();
        fieldErrors = new HashSet<String>();
        for(ConstraintViolation<Object> error : errors)
        {
            this.fieldErrors.add(error.getMessageTemplate());
        }
    }
    
    public SgDataValidationException()
    {
    }

    /**
     * @return the fieldErrors
     */
    public Set<String> getFieldErrors() {
        return fieldErrors;
    }

    /**
     * @param fieldErrors the fieldErrors to set
     */
    public void setFieldErrors(Set<String> fieldErrors) {
        this.fieldErrors = fieldErrors;
    }
}