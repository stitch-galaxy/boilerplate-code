/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.domain.services;

/**
 *
 * @author Admin
 */
public interface PasswordHasher {
    public String getHash(String password);
    
}
