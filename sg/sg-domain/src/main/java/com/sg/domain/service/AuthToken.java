/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.domain.service;

import com.sg.constants.Roles;
import com.sg.constants.TokenExpirationIntervals;
import com.sg.constants.TokenExpirationType;
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

    public AuthToken(Long userId, List<String> roles, TokenExpirationType expirationType) {
        this.userId = userId;
        List<String> authorities = new ArrayList<String>();
        for (String r : roles) {
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

    private Long userId;
    private List<String> authorities;

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

    /**
     * @return the userId
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * @param userId the userId to set
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
