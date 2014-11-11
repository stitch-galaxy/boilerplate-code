/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sg.dto.request;

import com.sg.dto.constraints.ThreadRequired;
import org.apache.commons.lang3.builder.EqualsBuilder;

/**
 *
 * @author tarasev
 */
public class ThreadUpdateDto {
    
    public static final String CODE_FIELD = "ThreadUpdateDto.Code";
    public static final String REF_CODE_FIELD = "ThreadUpdateDto.RefCode";
    
    @ThreadRequired(message = CODE_FIELD)
    private String code;
    
    @ThreadRequired(message = REF_CODE_FIELD)
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
