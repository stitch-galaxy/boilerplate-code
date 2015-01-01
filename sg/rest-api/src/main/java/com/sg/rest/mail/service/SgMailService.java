/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.rest.mail.service;

/**
 *
 * @author tarasev
 */
public interface SgMailService {

    public void sendEmailVerificationEmail(String token, String email);
}
