/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.mail.service;

/**
 *
 * @author tarasev
 */
public interface EmailService {

    public void sendVerificationEmail(String token, String email);
}
