/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sg.dto.request;

import com.sg.constants.DtoFieldCodes;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.hibernate.validator.constraints.NotBlank;

/**
 *
 * @author tarasev
 */
public class ThreadUpdateDto {
    
    @NotBlank(message = DtoFieldCodes.FIELD_THREAD_UPDATE_DTO_CODE)
    private String code;
    
    @NotBlank(message = DtoFieldCodes.FIELD_THREAD_UPDATE_DTO_REF_CODE)
    private String refCode;


    /**
     * @return the code
     */
    public String getCode() {
        return code;
    }

    /**
     * @param code the code to set
     */
    public void setCode(String code) {
        this.code = code;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        }

        ThreadUpdateDto other = (ThreadUpdateDto) obj;
        return new EqualsBuilder().
                append(this.code, other.code).
                append(this.getRefCode(), other.getRefCode()).
                isEquals();
    }

    /**
     * @return the refCode
     */
    public String getRefCode() {
        return refCode;
    }

    /**
     * @param refCode the refCode to set
     */
    public void setRefCode(String refCode) {
        this.refCode = refCode;
    }
    
}
