/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.stitchgalaxy.domain.entities.jpa;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author tarasev
 */
@Entity(name = "canvas")
public class Canvas {
    @Id
    @Column(name="code")
    private String code;
    
    @Column(name="stitches_per_inch", nullable = false)
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
    
}
