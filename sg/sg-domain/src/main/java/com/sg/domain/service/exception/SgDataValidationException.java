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

    private Set<String> errors = new HashSet<String>();
    
    public SgDataValidationException(Set<ConstraintViolation<Object>> errors) {
        super();
        for(ConstraintViolation<Object> error : errors)
        {
            this.errors.add(error.getMessageTemplate());
        }
    }

    /**
     * @return the errors
     */
    public Set<String> getErrors() {
        return errors;
    }
}