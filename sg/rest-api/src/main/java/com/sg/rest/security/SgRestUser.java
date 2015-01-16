/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.rest.security;

import com.sg.domain.enumerations.Role;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 *
 * @author tarasev
 */
public class SgRestUser implements UserDetails {

    private Collection<? extends GrantedAuthority> authorities;
    private final long accountId;

    public SgRestUser(long accountId) {
        this.accountId = accountId;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return String.valueOf(getAccountId());
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
    
    public void setRoles(Set<Role> roles) {
        List<GrantedAuthority> newAuthorities = new ArrayList<GrantedAuthority>();
        for(Role role : roles)
        {
            newAuthorities.add(new SimpleGrantedAuthority(SPRING_ROLE_ID_PREFIX + role.toString()));
        }
        this.authorities = newAuthorities;
    }
    private static final String SPRING_ROLE_ID_PREFIX = "ROLE_";

    /**
     * @return the accountId
     */
    public long getAccountId() {
        return accountId;
    }
}
