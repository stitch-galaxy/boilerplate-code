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
import com.sg.domain.exceptions.PasswordInvalidException;
import com.sg.domain.services.AccountManagementService;
import com.sg.domain.services.TokenBasedSecurityService;
import com.sg.rest.api.dto.ResetPasswordStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ResetPasswordController {

    public static final String URI = "/account/password/reset";

    public static final String TOKEN_PARAMETER = "token";
    public static final String PASSWORD_PARAMETER = "password";

    @Autowired
    private AccountManagementService accountManagementService;

    @Autowired
    private TokenBasedSecurityService security;

    @RequestMapping(value = URI, method = RequestMethod.GET)
    public ResetPasswordStatus verify(
            @RequestParam(value = TOKEN_PARAMETER) String sToken,
            @RequestParam(value = PASSWORD_PARAMETER) String password) {
        try {
            if (sToken == null) {
                throw new IllegalArgumentException();
            }
            Account account = security.getAccount(sToken, TokenBasedSecurityService.PASWORD_RESET_TOKEN);
            accountManagementService.resetPassword(account, password);
            return new ResetPasswordStatus(ResetPasswordStatus.Status.SUCCESS);
        } catch (NonEmailAccountException ex) {
            return new ResetPasswordStatus(ResetPasswordStatus.Status.INVALID_TOKEN);
        } catch (PasswordInvalidException ex) {
            return new ResetPasswordStatus(ResetPasswordStatus.Status.INVALID_PASSWORD);
        }
    }

}
