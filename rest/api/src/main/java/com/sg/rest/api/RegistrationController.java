/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.rest.api;

/**
 *
 * @author Admin
 */
import com.sg.domain.exceptions.EmailInvalidException;
import com.sg.domain.exceptions.EmailIsNotUniqueException;
import com.sg.domain.exceptions.PasswordInvalidException;
import com.sg.domain.services.AccountManagementService;
import com.sg.rest.api.dto.RegistrationStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegistrationController {

    public static final String URI = "/account/create";
    public static final String EMAIL_PARAMETER = "email";
    public static final String PASSWORD_PARAMETER = "password";

    @Autowired
    private AccountManagementService accountManagementService;

    @RequestMapping(value = URI, method = RequestMethod.GET)
    public RegistrationStatus signup(
            @RequestParam(value = EMAIL_PARAMETER) String email,
            @RequestParam(value = PASSWORD_PARAMETER) String password) {
        if (email == null || password == null) {
            throw new IllegalArgumentException();
        }
        try {
            accountManagementService.registerEmailAccount(email, password);
            return new RegistrationStatus(RegistrationStatus.Status.SUCCESS);

        } catch (EmailIsNotUniqueException ex) {
            return new RegistrationStatus(RegistrationStatus.Status.EMAIL_ALREADY_REGISTERED);
        } catch (EmailInvalidException ex) {
            return new RegistrationStatus(RegistrationStatus.Status.EMAIL_INVALID);
        } catch (PasswordInvalidException ex) {
            return new RegistrationStatus(RegistrationStatus.Status.PASSWORD_INVALID);
        }
    }

}
