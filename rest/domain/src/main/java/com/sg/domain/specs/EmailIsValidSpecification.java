/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.domain.specs;

import com.sg.domain.vo.Email;
import org.apache.commons.validator.EmailValidator;

/**
 *
 * @author Admin
 */
public class EmailIsValidSpecification {

    private static final EmailValidator emailValidator = EmailValidator.getInstance();

    public EmailIsValidSpecification() {
    }

    public boolean isSatisfiedBy(Email email) {
        return emailValidator.isValid(email.getEmail());
    }
}
