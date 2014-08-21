/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.dto.request;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.hibernate.validator.constraints.NotBlank;

/**
 *
 * @author tarasev
 */
public class ThreadDeleteDto {

    public static final String FIELD_THREAD_CODE = "ThreadDeleteDto.Code";
    
    @NotBlank(message = FIELD_THREAD_CODE)
    private String code;

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

        ThreadDeleteDto other = (ThreadDeleteDto) obj;
        return new EqualsBuilder().
                append(this.code, other.code).
                isEquals();
    }
}
