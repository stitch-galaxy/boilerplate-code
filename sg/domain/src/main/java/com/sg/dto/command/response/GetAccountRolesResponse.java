/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.dto.command.response;

import com.sg.domain.enumerations.Role;
import com.sg.dto.enumerations.GetAccountRolesStatus;
import java.util.Set;

/**
 *
 * @author tarasev
 */
public class GetAccountRolesResponse implements CommandResponse {
 
    private final Set<Role> roles;
    private final GetAccountRolesStatus status;

    public GetAccountRolesResponse(Set<Role> roles) {
        if (roles == null) {
            throw new IllegalArgumentException();
        }
        this.roles = roles;
        this.status = GetAccountRolesStatus.STATUS_SUCCESS;
    }
    
    public GetAccountRolesResponse(GetAccountRolesStatus status)
    {
        if (status != GetAccountRolesStatus.STATUS_ACCOUNT_NOT_FOUND)
        {
            throw new IllegalArgumentException();
        }
        this.status = status;
        this.roles = null;
    }

    /**
     * @return the roles
     */
    public Set<Role> getRoles() {
        return roles;
    }

    /**
     * @return the status
     */
    public GetAccountRolesStatus getStatus() {
        return status;
    }
}
