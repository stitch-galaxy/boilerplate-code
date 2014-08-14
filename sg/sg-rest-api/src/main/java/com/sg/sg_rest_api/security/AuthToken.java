/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.sg_rest_api.security;

import com.sg.constants.Roles;
import com.sg.constants.TokenExpirationIntervals;
import com.sg.constants.TokenExpirationType;
import com.sg.dto.UserDto;
import java.util.ArrayList;
import java.util.List;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.joda.time.Instant;

/**
 *
 * @author tarasev
 */
public class AuthToken {

    //TODO: add expiration
    public AuthToken() {
    }

    private long expirationMillis;

    public AuthToken(UserDto dto, TokenExpirationType expirationType) {
        this.email = dto.getEmail();
        List<String> authorities = new ArrayList<String>();
        for (String r : dto.getRoles()) {
            authorities.add(Roles.ROLE_AUTHORITY_PREFIX + r);
        }
        setAuthorities(authorities);

        Instant now = Instant.now();
        expirationMillis = now.getMillis();
        switch (expirationType) {
            case LONG_TOKEN:
                expirationMillis += TokenExpirationIntervals.LONG_TOKEN_EXPIRATION_INTERVAL;
                break;
            case USER_SESSION_TOKEN:
                expirationMillis += TokenExpirationIntervals.USER_SESSION_TOKEN_EXPIRATION_INTERVAL;
                break;
            case NEVER_EXPIRES:
                expirationMillis = 0;
                break;
        }
    }

    private String email;
    private List<String> authorities;

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the authorities
     */
    public List<String> getAuthorities() {
        return authorities;
    }

    /**
     * @param authorities the authorities to set
     */
    public void setAuthorities(List<String> authorities) {
        this.authorities = authorities;
    }

    @JsonIgnore
    public boolean isExpired() {
        if (expirationMillis == 0) {
            return false;
        }
        if (expirationMillis > Instant.now().getMillis()) {
            return false;
        }
        return true;
    }

    /**
     * @return the expirationMillis
     */
    public long getExpirationMillis() {
        return expirationMillis;
    }

    /**
     * @param expirationMillis the expirationMillis to set
     */
    public void setExpirationMillis(long expirationMillis) {
        this.expirationMillis = expirationMillis;
    }
}
