/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.rest.api;

import com.sg.rest.api.dto.LoginStatus;
import com.sg.rest.api.security.AppSecurityService;
import java.nio.charset.Charset;
import java.util.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Admin
 */
@RestController
public class LoginController {

    public static final String URI = "/oauth/token";

    @Autowired
    private AppSecurityService security;

    @RequestMapping(value = URI, method = RequestMethod.GET)
    public LoginStatus login(
            @RequestHeader(value = HttpHeaders.AUTHORIZATION) String authorization) {
        if (authorization == null || !authorization.startsWith("Basic")) {
            throw new IllegalArgumentException();
        }

        String base64Credentials = authorization.substring("Basic".length()).trim();
        String credentials = new String(Base64.getDecoder().decode(base64Credentials),
                Charset.forName("UTF-8"));
        final String[] values = credentials.split(":", 2);
        if (values.length != 2) {
            throw new IllegalArgumentException();
        }

        return security.login(values[0], values[1]);
    }
}
