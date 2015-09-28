/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.domain.authtoken.jwt;

import java.util.UUID;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 *
 * @author Admin
 */
public class JwtToken {

    private final UUID accountId;
    private final int tokenType;
    private final UUID tokenIdentity;

    public JwtToken(UUID uid,
                    int type,
                    UUID tokenIdentity) {
        if (uid == null || tokenIdentity == null) {
            throw new IllegalArgumentException();
        }
        this.accountId = uid;
        this.tokenType = type;
        this.tokenIdentity = tokenIdentity;
    }

    /**
     * @return the accountId
     */
    public UUID getAccountId() {
        return accountId;
    }

    /**
     * @return the tokenType
     */
    public int getTokenType() {
        return tokenType;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        }

        JwtToken other = (JwtToken) obj;
        return new EqualsBuilder().
                append(this.accountId, other.accountId).
                append(this.tokenType, other.tokenType).
                append(this.tokenIdentity, other.tokenIdentity).
                isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(accountId).append(tokenType).append(tokenIdentity).hashCode();
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    /**
     * @return the tokenIdentity
     */
    public UUID getTokenIdentity() {
        return tokenIdentity;
    }

}
