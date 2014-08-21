/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.dto.response;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.builder.EqualsBuilder;

/**
 *
 * @author tarasev
 */
public class CanvasesListDto {

    public static class CanvasInfo {

        private String code;
        private BigDecimal stitchesPerInch;

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || this.getClass() != obj.getClass()) {
                return false;
            }

            CanvasInfo other = (CanvasInfo) obj;
            return new EqualsBuilder().
                    append(this.getCode(), other.getCode()).
                    isEquals()
                    && this.getStitchesPerInch().compareTo(other.getStitchesPerInch()) == 0;

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

    private List<CanvasInfo> canvases;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        }

        CanvasesListDto other = (CanvasesListDto) obj;
        return new EqualsBuilder().
                append(this.canvases, other.canvases).
                isEquals();

    }
    
    public List<CanvasInfo> getCanvases() {
        return canvases;
    }


    public void setCanvases(List<CanvasInfo> canvases) {
        this.canvases = canvases;
    }

}
