/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.domain.services;

import com.sg.domain.vo.Email;
import com.sg.domain.vo.TokenSignature;

/**
 *
 * @author Admin
 */
public interface MailService {

    public void sendRegistrationConfirmationEmail(Email email,
                                             TokenSignature signature);
    
    public void sendResetPasswordLink(Email email,
                                             TokenSignature signature);

}
