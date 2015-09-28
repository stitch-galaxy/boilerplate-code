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
import com.sg.domain.exceptions.EmailNotRegisteredException;
import com.sg.domain.services.AccountManagementService;
import com.sg.rest.api.dto.SendResetPassowordLinkStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SendResetPasswordLinkController {

    public static final String URI = "/account/password/reset/email";
    public static final String EMAIL_PARAMETER = "email";

    @Autowired
    private AccountManagementService accountManagementService;

    @RequestMapping(value = URI, method = RequestMethod.GET)
    public SendResetPassowordLinkStatus resendRegistrationConfirationEmail(
            @RequestParam(value = EMAIL_PARAMETER) String email) {
        if (email == null) {
            throw new IllegalArgumentException();
        }
        try {
            accountManagementService.sendPasswordResetLink(email);
            return new SendResetPassowordLinkStatus(SendResetPassowordLinkStatus.Status.SUCCESS);

        } catch (EmailNotRegisteredException ex) {
            return new SendResetPassowordLinkStatus(SendResetPassowordLinkStatus.Status.EMAIL_NOT_REGISTERED);
        }
    }

}
