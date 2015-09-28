/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.rest.api;

import com.sg.domain.ar.Account;
import com.sg.domain.exceptions.NonEmailAccountException;
import com.sg.domain.exceptions.PasswordDoNotMatchException;
import com.sg.domain.exceptions.PasswordInvalidException;
import com.sg.domain.services.AccountManagementService;
import com.sg.domain.services.TokenBasedSecurityService;
import com.sg.rest.api.dto.ChangePasswordStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Admin
 */
@RestController
public class ChangePasswordController {

    public static final String URI = "/account/password/change";
    public static final String TOKEN_PARAMETER = "token";
    public static final String OLD_PASSWORD_PARAMETER = "old";
    public static final String NEW_PASSWORD_PARAMETER = "new";

    @Autowired
    private TokenBasedSecurityService security;

    @Autowired
    private AccountManagementService accountManagementService;

    @RequestMapping(value = URI, method = RequestMethod.GET)
    public ChangePasswordStatus changePassword(
            @RequestParam(value = TOKEN_PARAMETER) String sToken,
            @RequestParam(value = OLD_PASSWORD_PARAMETER) String oldPassword,
            @RequestParam(value = NEW_PASSWORD_PARAMETER) String newPassword
    ) {
        Account account = security.getAccount(sToken, TokenBasedSecurityService.APP_TOKEN);
        try {
            accountManagementService.changePassword(account, oldPassword, newPassword);
            return new ChangePasswordStatus(ChangePasswordStatus.Status.SUCCESS);
        } catch (NonEmailAccountException ex) {
            return new ChangePasswordStatus(ChangePasswordStatus.Status.NOT_EMAIL_ACCOUNT);
        } catch (PasswordDoNotMatchException ex) {
            return new ChangePasswordStatus(ChangePasswordStatus.Status.OLD_PASSWORD_INCORRECT);
        } catch (PasswordInvalidException ex) {
            return new ChangePasswordStatus(ChangePasswordStatus.Status.NEW_PASSWORD_INVALID);
        }
    }

}
