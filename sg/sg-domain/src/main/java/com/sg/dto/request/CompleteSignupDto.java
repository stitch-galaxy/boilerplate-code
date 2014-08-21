/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sg.dto.request;

import com.sg.constants.PasswordRegexp;
import javax.validation.constraints.Pattern;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

/**
 *
 * @author tarasev
 */
public class CompleteSignupDto {
    public static final String FIELD_COMPLETE_SIGNUP_PASSWORD = "CompleteSignupDto.Password";
    @NotBlank(message = FIELD_COMPLETE_SIGNUP_PASSWORD)
    @Pattern(regexp = PasswordRegexp.PASSWORD_REGULAR_EXPRESSION, message = FIELD_COMPLETE_SIGNUP_PASSWORD)
    private String password;
    
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        }

        CompleteSignupDto other = (CompleteSignupDto) obj;
        return new EqualsBuilder().
                append(this.getPassword(), other.getPassword()).
                isEquals();

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
}
