/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.domail.vo;

import com.sg.domain.enumerations.Role;
import java.util.EnumSet;
import java.util.Set;

/**
 *
 * @author tarasev
 */
public class Permissions {

    private final Set<Role> roles;

    private Permissions(Set<Role> roles)
    {
        this.roles = roles;
    }
    
    public Permissions()
    {
        this.roles = EnumSet.noneOf(Role.class);
    }
    
    public Permissions addRole(Role role)
    {
        Set<Role> newRoles = EnumSet.noneOf(Role.class);
        newRoles.addAll(this.roles);
        newRoles.add(role);
        return new Permissions(newRoles);
    }
    
    public Permissions revokeRole(Role role)
    {
        Set<Role> newRoles = EnumSet.noneOf(Role.class);
        newRoles.addAll(this.roles);
        newRoles.remove(role);
        return new Permissions(newRoles);
    }
    
    public Set<Role> getAllRoles()
    {
        Set<Role> allRoles = EnumSet.noneOf(Role.class);
        allRoles.addAll(this.roles);
        return allRoles;
    }
}
