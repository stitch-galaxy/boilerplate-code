/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.dto.request;

import com.sg.dto.constraints.EmailRequired;
import com.sg.dto.constraints.PasswordRequired;
import org.apache.commons.lang3.builder.EqualsBuilder;

/**
 *
 * @author tarasev
 */
public class SigninDto {

    public static final String EMAIL_FIELD = "SigninDto.Email";
    public static final String PASSWORD_FIELD = "SigninDto.Password";
    
    @EmailRequired(message = EMAIL_FIELD)
    private String email;

    @PasswordRequired(message = PASSWORD_FIELD)
    private String password;

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
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        }

        SigninDto other = (SigninDto) obj;
        return new EqualsBuilder().
                append(this.email, other.email).
                append(this.password, other.password).
                isEquals();
    }
}
