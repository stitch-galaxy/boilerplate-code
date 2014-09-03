/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.dto.request;

import com.sg.constants.DtoFieldCodes;
import java.math.BigDecimal;
import javax.validation.constraints.NotNull;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.hibernate.validator.constraints.NotBlank;

/**
 *
 * @author tarasev
 */
public class CanvasUpdateDto {

    @NotBlank(message = DtoFieldCodes.FIELD_UPDATE_CANVAS_DTO_REF_CODE)
    private String refCode;

    @NotBlank(message = DtoFieldCodes.FIELD_UPDATE_CANVAS_DTO_CODE)
    private String code;

    @NotNull(message = DtoFieldCodes.FIELD_UPDATE_CANVAS_DTO_STITCHES_PER_INCH)
    private BigDecimal stitchesPerInch;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        }

        CanvasUpdateDto other = (CanvasUpdateDto) obj;
        return new EqualsBuilder().
                append(this.getRefCode(), other.getRefCode()).
                append(this.getCode(), other.getCode()).
                isEquals()
                && this.getStitchesPerInch().compareTo(other.getStitchesPerInch()) == 0;
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

}
