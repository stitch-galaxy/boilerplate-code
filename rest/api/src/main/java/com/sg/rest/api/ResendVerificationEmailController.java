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
import com.sg.domain.exceptions.EmailAlreadyVerifiedException;
import com.sg.domain.exceptions.EmailNotRegisteredException;
import com.sg.domain.services.AccountRegistrationService;
import com.sg.rest.api.dto.ResendVerificationEmailStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ResendVerificationEmailController {

    public static final String URI = "/signup/email/resend";
    public static final String EMAIL_PARAMETER = "email";

    @Autowired
    private AccountRegistrationService accountRegistrationService;

    @RequestMapping(value = URI, method = RequestMethod.GET)
    public ResendVerificationEmailStatus signup(
            @RequestParam(value = EMAIL_PARAMETER) String email) {
        if (email == null) {
            throw new IllegalArgumentException();
        }
        try {
            accountRegistrationService.resendVerificationEmail(email);
            return new ResendVerificationEmailStatus(ResendVerificationEmailStatus.Status.SUCCESS);

        } catch (EmailNotRegisteredException ex) {
            return new ResendVerificationEmailStatus(ResendVerificationEmailStatus.Status.EMAIL_NOT_REGISTERED);
        } catch (EmailAlreadyVerifiedException ex) {
            return new ResendVerificationEmailStatus(ResendVerificationEmailStatus.Status.EMAIL_ALREADY_VERIFIED);
        }
    }

}
