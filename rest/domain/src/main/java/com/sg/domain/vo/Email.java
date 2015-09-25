/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.domain.vo;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 *
 * @author Admin
 */
public class Email {

    private final String email;

    public Email(String email) {
        this.email = email;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        }

        Email other = (Email) obj;
        return new EqualsBuilder().
                append(this.email, other.email).
                isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(email).hashCode();
    }
}
