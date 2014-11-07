/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.rest.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 *
 * @author tarasev
 */
public class SgRestUser implements UserDetails {

    private Collection<? extends GrantedAuthority> authorities;
    private final Long accountId;

    public SgRestUser(Long accountId) {
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
        return getAccountId().toString();
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
    
    public void setRoles(Collection<String> roles) {
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        for(String role : roles)
        {
            authorities.add(new SimpleGrantedAuthority(SPRING_ROLE_ID_PREFIX + role));
        }
        this.authorities = authorities;
    }
    private static final String SPRING_ROLE_ID_PREFIX = "ROLE_";

    /**
     * @return the accountId
     */
    public Long getAccountId() {
        return accountId;
    }
}
