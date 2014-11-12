/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.dto.request;

import com.sg.dto.constraints.EmailRequired;
import com.sg.dto.constraints.UserFirstName;
import com.sg.dto.constraints.UserLastName;
import org.apache.commons.lang3.builder.EqualsBuilder;

/**
 *
 * @author tarasev
 */
public class SignupDto {

    public static final String EMAIL_FIELD = "SignupDto.Email";
    public static final String USER_FIRSTNAME_FIELD = "SignupDto.UserFirstName";
    public static final String USER_LASTNAME_FIELD = "SignupDto.UserLastName";
    
    @EmailRequired(message = EMAIL_FIELD)
    private String email;

    @UserFirstName(message = USER_FIRSTNAME_FIELD)
    private String userFirstName;

    @UserLastName(message = USER_LASTNAME_FIELD)
    private String userLastName;

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

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        }

        SignupDto other = (SignupDto) obj;
        return new EqualsBuilder().
                append(this.email, other.email).
                append(this.userFirstName, other.userFirstName).
                append(this.userLastName, other.userLastName).
                isEquals();

    }

    /**
     * @return the userFirstName
     */
    public String getUserFirstName() {
        return userFirstName;
    }

    /**
     * @param userFirstName the userFirstName to set
     */
    public void setUserFirstName(String userFirstName) {
        this.userFirstName = userFirstName;
    }

    /**
     * @return the userLastName
     */
    public String getUserLastName() {
        return userLastName;
    }

    /**
     * @param userLastName the userLastName to set
     */
    public void setUserLastName(String userLastName) {
        this.userLastName = userLastName;
    }
}
