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
import com.sg.rest.api.dto.ResendRegistrationConfirmationEmailStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ResendRegistrationConfirmationEmailController {

    public static final String URI = "/account/create/resendEmail";
    public static final String EMAIL_PARAMETER = "email";

    @Autowired
    private AccountRegistrationService accountRegistrationService;

    @RequestMapping(value = URI, method = RequestMethod.GET)
    public ResendRegistrationConfirmationEmailStatus resendRegistrationConfirationEmail(
            @RequestParam(value = EMAIL_PARAMETER) String email) {
        if (email == null) {
            throw new IllegalArgumentException();
        }
        try {
            accountRegistrationService.resendVerificationEmail(email);
            return new ResendRegistrationConfirmationEmailStatus(ResendRegistrationConfirmationEmailStatus.Status.SUCCESS);

        } catch (EmailNotRegisteredException ex) {
            return new ResendRegistrationConfirmationEmailStatus(ResendRegistrationConfirmationEmailStatus.Status.EMAIL_NOT_REGISTERED);
        } catch (EmailAlreadyVerifiedException ex) {
            return new ResendRegistrationConfirmationEmailStatus(ResendRegistrationConfirmationEmailStatus.Status.EMAIL_ALREADY_VERIFIED);
        }
    }

}
