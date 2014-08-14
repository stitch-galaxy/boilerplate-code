/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.sg_rest_api.security;

import com.sg.constants.Roles;
import com.sg.dto.UserDto;
import java.util.ArrayList;
import java.util.List;
import org.codehaus.jackson.annotate.JsonIgnore;

/**
 *
 * @author tarasev
 */
public class AuthToken {

    //TODO: add expiration
    public AuthToken(){
    }
    
    
    public AuthToken(UserDto dto) {
        this.email = dto.getEmail();
        List<String> authorities = new ArrayList<String>();
        for (String r : dto.getRoles()) {
            authorities.add(Roles.ROLE_AUTHORITY_PREFIX + r);
        }
        setAuthorities(authorities);
    }

    private String email;
    private List<String> authorities;

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the authorities
     */
    public List<String> getAuthorities() {
        return authorities;
    }

    /**
     * @param authorities the authorities to set
     */
    public void setAuthorities(List<String> authorities) {
        this.authorities = authorities;
    }
    
    @JsonIgnore
    public boolean isExpired()
    {
        return false;
    }
}
