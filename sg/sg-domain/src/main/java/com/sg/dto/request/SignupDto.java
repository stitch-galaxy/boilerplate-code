/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.dto.request;

import com.sg.constants.DtoFieldCodes;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.text.WordUtils;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

/**
 *
 * @author tarasev
 */
public class SignupDto {

    @NotBlank(message = DtoFieldCodes.FIELD_SIGNUP_DTO_USER_EMAIL)
    @Email(message = DtoFieldCodes.FIELD_SIGNUP_DTO_USER_EMAIL)
    private String email;

    @NotBlank(message = DtoFieldCodes.FIELD_SIGNUP_DTO_USER_FIRST_NAME)
    private String userFirstName;

    @NotBlank(message = DtoFieldCodes.FIELD_SIGNUP_DTO_USER_LAST_NAME)
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
