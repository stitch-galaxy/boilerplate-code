/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.rest.mail.service;

import com.sg.mail.service.EmailService;
import org.springframework.stereotype.Service;

/**
 *
 * @author tarasev
 */
@Service
public class SgMailServiceImpl implements EmailService {

    @Override
    public void sendVerificationEmail(String token, String email) {
    }

}
