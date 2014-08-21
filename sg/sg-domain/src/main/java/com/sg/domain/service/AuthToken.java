/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.domain.service;

import com.sg.constants.Roles;
import com.sg.constants.TokenExpirationIntervals;
import com.sg.constants.TokenExpirationType;
import com.sg.dto.response.AccountDto;
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

    private long expirationInstantMillis;

    public AuthToken(AccountDto dto, TokenExpirationType expirationType, Instant issueTime) {
        this.accountId = dto.getId();
        List<String> authoritiesToGrantAccess = new ArrayList<String>();
        for (String r : dto.getRoles()) {
            authoritiesToGrantAccess.add(Roles.ROLE_AUTHORITY_PREFIX + r);
        }
        this.authorities = authoritiesToGrantAccess;
        this.expirationInstantMillis = issueTime.getMillis();
        switch (expirationType) {
            case LONG_TOKEN:
                this.expirationInstantMillis += TokenExpirationIntervals.LONG_TOKEN_EXPIRATION_INTERVAL.getMillis();
                break;
            case USER_SESSION_TOKEN:
                this.expirationInstantMillis += TokenExpirationIntervals.USER_SESSION_TOKEN_EXPIRATION_INTERVAL.getMillis();
                break;
            case NEVER_EXPIRES:
                expirationInstantMillis = Long.MAX_VALUE;
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
    public boolean isExpired(Instant moment) {
        
        if (expirationInstantMillis > moment.getMillis()) {
            return false;
        }
        return true;
    }

    /**
     * @return the expirationInstantMillis
     */
    public long getExpirationInstantMillis() {
        return expirationInstantMillis;
    }

    /**
     * @param expirationInstantMillis the expirationInstantMillis to set
     */
    public void setExpirationInstantMillis(long expirationInstantMillis) {
        this.expirationInstantMillis = expirationInstantMillis;
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
                append(this.expirationInstantMillis, other.expirationInstantMillis).
                isEquals();
    }
}
