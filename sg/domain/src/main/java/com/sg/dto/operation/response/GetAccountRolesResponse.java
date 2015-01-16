/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.dto.operation.response;

import com.sg.domain.enumerations.Role;
import com.sg.dto.operation.status.GetAccountRolesStatus;
import java.util.Set;

/**
 *
 * @author tarasev
 */
public class GetAccountRolesResponse implements Response {

    /**
     * @return the data
     */
    public Data getData() {
        return data;
    }

    public static class Data {

        private final Set<Role> roles;

        public Data(Set<Role> roles) {
            if (roles == null) {
                throw new IllegalArgumentException();
            }
            this.roles = roles;
        }

        /**
         * @return the token
         */
        public Set<Role> getRoles() {
            return roles;
        }

    }

    private final GetAccountRolesStatus status;
    private final Data data;

    public GetAccountRolesResponse(Set<Role> roles) {
        this.status = GetAccountRolesStatus.STATUS_SUCCESS;
        this.data = new Data(roles);
    }

    public GetAccountRolesResponse(GetAccountRolesStatus status) {
        if (status != GetAccountRolesStatus.STATUS_ACCOUNT_NOT_FOUND) {
            throw new IllegalArgumentException();
        }
        this.status = status;
        this.data = null;
    }

    /**
     * @return the status
     */
    public GetAccountRolesStatus getStatus() {
        return status;
    }
}
