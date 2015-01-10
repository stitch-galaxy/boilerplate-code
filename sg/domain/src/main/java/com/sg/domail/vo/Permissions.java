/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.domail.vo;

import java.util.List;

/**
 *
 * @author tarasev
 */
public class Permissions {

    private final List<String> roles;

    public Permissions(List<String> roles) {
        if (roles == null || roles.size() <= 0) {
            throw new IllegalArgumentException();
        }
        this.roles = roles;
    }

    /**
     * @return the roles
     */
    public List<String> getRoles() {
        return roles;
    }
}
