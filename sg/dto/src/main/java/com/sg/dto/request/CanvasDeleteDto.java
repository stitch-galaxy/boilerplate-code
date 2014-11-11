/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.dto.request;

import com.sg.dto.constraints.CanvasRequired;
import org.apache.commons.lang3.builder.EqualsBuilder;

/**
 *
 * @author tarasev
 */
public class CanvasDeleteDto {

    public static final String CODE_FIELD = "CanvasDeleteDto.Code";
    
    @CanvasRequired(message = CODE_FIELD)
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

        CanvasDeleteDto other = (CanvasDeleteDto) obj;
        return new EqualsBuilder().
                append(this.code, other.code).
                isEquals();
    }
}
