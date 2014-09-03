/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sg.dto.request;

import com.sg.constants.DtoFieldCodes;
import com.sg.constants.PasswordRegexp;
import javax.validation.constraints.Pattern;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.hibernate.validator.constraints.NotBlank;

/**
 *
 * @author tarasev
 */
public class ResetPasswordDto {
    @NotBlank(message = DtoFieldCodes.FIELD_RESET_PASSWORD_DTO_PASSWORD)
    @Pattern(regexp = PasswordRegexp.PASSWORD_REGULAR_EXPRESSION, message = DtoFieldCodes.FIELD_RESET_PASSWORD_DTO_PASSWORD)
    private String password;
    
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        }

        ResetPasswordDto other = (ResetPasswordDto) obj;
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
