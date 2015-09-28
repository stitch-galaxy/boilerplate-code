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
import com.sg.domain.ar.Account;
import com.sg.domain.exceptions.NonEmailAccountException;
import com.sg.domain.exceptions.EmailAlreadyVerifiedException;
import com.sg.domain.services.AccountRegistrationService;
import com.sg.domain.services.TokenBasedSecurityService;
import com.sg.rest.api.dto.RegistrationConfirmationStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegistrationConfirmationController {

    public static final String URI = "/account/verify";
    
    public static final String TOKEN_PARAMETER = "token";

    @Autowired
    private AccountRegistrationService accountRegistrationService;
    
    @Autowired
    private TokenBasedSecurityService security;

    @RequestMapping(value = URI, method = RequestMethod.GET)
    public RegistrationConfirmationStatus verify(@RequestParam(value = TOKEN_PARAMETER) String sToken) {
        try {
            if (sToken == null) {
                throw new IllegalArgumentException();
            }
            Account account = security.getTokenAccount(sToken);
            accountRegistrationService.verify(account);
            return new RegistrationConfirmationStatus(RegistrationConfirmationStatus.Status.SUCCESS);
        } catch (NonEmailAccountException ex) {
            return new RegistrationConfirmationStatus(RegistrationConfirmationStatus.Status.INVALID_TOKEN);
        } catch (EmailAlreadyVerifiedException ex) {
            return new RegistrationConfirmationStatus(RegistrationConfirmationStatus.Status.EMAIL_ALREADY_VERIFIED);
        }
        
    }

}
