/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sg.domain.service;

import com.sg.domain.service.exception.SgDataValidationException;
import java.util.Set;
import javax.annotation.Resource;
import javax.validation.ConstraintViolation;
//import org.springframework.validation.Validator;
import javax.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author tarasev
 */
public class ValidatorComponent {
    
    @Autowired
    private Validator validator;
    
    public void validate(Object object) throws SgDataValidationException
    {
        Set<ConstraintViolation<Object>> errors = validator.validate(object);
        if (errors != null && errors.size() > 0)
        {
            throw new SgDataValidationException(errors);
        }
    }
    
}
