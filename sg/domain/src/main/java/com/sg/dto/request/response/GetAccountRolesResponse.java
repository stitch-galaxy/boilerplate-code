/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.dto.request.response;

import com.sg.domain.enumerations.Role;
import java.util.Set;

/**
 *
 * @author tarasev
 */
public class GetAccountRolesResponse implements RequestResponse {
 
    private final Set<Role> roles;

    public GetAccountRolesResponse(Set<Role> roles) {
        if (roles == null) {
            throw new IllegalArgumentException();
        }
        this.roles = roles;
    }

    /**
     * @return the roles
     */
    public Set<Role> getRoles() {
        return roles;
    }
}
