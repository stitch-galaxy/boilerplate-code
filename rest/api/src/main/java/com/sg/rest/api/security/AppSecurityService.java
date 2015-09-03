/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.rest.api.security;

import com.sg.rest.api.security.exceptions.AppSecurityBadTokenException;
import com.sg.rest.api.security.exceptions.AppSecurityTokenExpiredException;

/**
 *
 * @author Admin
 */
public interface AppSecurityService {
    public void verifyToken(String sToken) throws AppSecurityBadTokenException, AppSecurityTokenExpiredException;
    public String generateWebToken(String uid);
}
