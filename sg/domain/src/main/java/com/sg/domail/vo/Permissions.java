/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.domail.vo;

import com.sg.domain.enumerations.Role;
import java.util.Collection;
import java.util.EnumSet;
import java.util.Set;

/**
 *
 * @author tarasev
 */
public class Permissions {

    private final Set<Role> roles;

    private Permissions(Collection<Role> roles)
    {
        this.roles = EnumSet.noneOf(Role.class);
        this.roles.addAll(roles);
    }
    
    public Permissions()
    {
        this.roles = EnumSet.noneOf(Role.class);
    }
    
    public void addRole(Role role)
    {
        roles.add(role);
    }
    
    public void addRoles(Collection<Role> roles)
    {
        roles.addAll(roles);
    }
    
    public void revokeRole(Role role)
    {
        roles.remove(role);
    }
    
    public Set<Role> getRoles()
    {
        Set<Role> allRoles = EnumSet.noneOf(Role.class);
        allRoles.addAll(this.roles);
        return allRoles;
    }
}
