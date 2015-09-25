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
public class AccountId extends UuidId {

    public static AccountId create() {
        return new AccountId(UUID.randomUUID());
    }

    public AccountId(UUID id) {
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

        AccountId other = (AccountId) obj;
        return new EqualsBuilder().
                append(this.id, other.id).
                isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(id).hashCode();
    }

}
