/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.domail.vo;

import com.sg.domain.enumerations.Role;
import java.util.List;

/**
 *
 * @author tarasev
 */
public class Permissions {

    private final List<Role> roles;

    public Permissions(List<Role> roles) {
        if (roles == null || roles.size() <= 0) {
            throw new IllegalArgumentException();
        }
        this.roles = roles;
    }

    /**
     * @return the roles
     */
    public List<Role> getRoles() {
        return roles;
    }
}
