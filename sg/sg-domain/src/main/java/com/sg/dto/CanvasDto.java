/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sg.dto;

import java.math.BigDecimal;
import org.apache.commons.lang3.builder.EqualsBuilder;

/**
 *
 * @author tarasev
 */
public class CanvasDto {
    private String code;
    private BigDecimal stitchesPerInch;

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

    /**
     * @return the stitchesPerInch
     */
    public BigDecimal getStitchesPerInch() {
        return stitchesPerInch;
    }

    /**
     * @param stitchesPerInch the stitchesPerInch to set
     */
    public void setStitchesPerInch(BigDecimal stitchesPerInch) {
        this.stitchesPerInch = stitchesPerInch;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        }

        CanvasDto other = (CanvasDto) obj;
        return new EqualsBuilder().
                append(this.code, other.code).
                isEquals()
                && this.stitchesPerInch.compareTo(other.stitchesPerInch) == 0;
    }
}
