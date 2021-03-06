/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.dto.operation;

import com.sg.dto.operation.Operation;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 *
 * @author tarasev
 */
public class GetAccountRolesOperation implements Operation {

    private final long accountId;
    
    public GetAccountRolesOperation(long accountId)
    {
        this.accountId = accountId;
    }

    /**
     * @return the accountId
     */
    public Long getAccountId() {
        return accountId;
    }
    
        @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        }

        GetAccountRolesOperation other = (GetAccountRolesOperation) obj;
        return new EqualsBuilder().
                append(this.accountId, other.accountId).
                isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(accountId).build();
    }
}
