/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.domain.vo;

import java.util.UUID;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 *
 * @author Admin
 */
public class TokenIdentity extends UuidId {

    public static TokenIdentity create() {
        return new TokenIdentity(UUID.randomUUID());
    }

    public TokenIdentity(UUID id) {
        super(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        }

        TokenIdentity other = (TokenIdentity) obj;
        return new EqualsBuilder().
                append(this.id, other.id).
                isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(id).hashCode();
    }

}
