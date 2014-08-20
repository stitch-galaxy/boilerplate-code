/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.domain.service;

import com.sg.constants.Roles;
import com.sg.constants.TokenExpirationIntervals;
import com.sg.constants.TokenExpirationType;
import com.sg.dto.AccountDto;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.joda.time.Instant;

/**
 *
 * @author tarasev
 */
public class AuthToken {

    //Need public constructor for JSON serialization
    public AuthToken() {
    }

    private long expirationMillis;

    public AuthToken(AccountDto dto, TokenExpirationType expirationType) {
        this.accountId = dto.getId();
        List<String> authoritiesToGrantAccess = new ArrayList<String>();
        for (String r : dto.getRoles()) {
            authoritiesToGrantAccess.add(Roles.ROLE_AUTHORITY_PREFIX + r);
        }
        this.authorities = authoritiesToGrantAccess;
        Instant now = Instant.now();
        this.expirationMillis = now.getMillis();
        long interval = 0;
        switch (expirationType) {
            case LONG_TOKEN:
                interval = TokenExpirationIntervals.LONG_TOKEN_EXPIRATION_INTERVAL;
                this.expirationMillis += interval;
                break;
            case USER_SESSION_TOKEN:
                interval = TokenExpirationIntervals.USER_SESSION_TOKEN_EXPIRATION_INTERVAL;
                this.expirationMillis += interval;
                break;
            case NEVER_EXPIRES:
                expirationMillis = 0;
                break;
        }
    }

    private Long accountId;
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
     * @return the accountId
     */
    public Long getAccountId() {
        return accountId;
    }

    /**
     * @param accountId the accountId to set
     */
    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        }

        AuthToken other = (AuthToken) obj;
        return new EqualsBuilder().
                append(this.accountId, other.accountId).
                append(this.authorities, other.authorities).
                append(this.expirationMillis, other.expirationMillis).
                isEquals();
    }
}
