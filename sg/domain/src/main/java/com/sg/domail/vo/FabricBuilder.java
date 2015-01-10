/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.domail.vo;

import java.math.BigDecimal;

public class FabricBuilder {

    private String fabricStyle;
    private BigDecimal count;
    private String dmcColor;

    public FabricBuilder(String fabricStyle,
            BigDecimal count,
            String dmcColor) {
        if (fabricStyle == null || count == null || dmcColor == null) {
            throw new IllegalArgumentException();
        }
        this.fabricStyle = fabricStyle;
        this.count = count;
        this.dmcColor = dmcColor;
    }

    public Fabric createFabric() {
        return new Fabric(this);
    }

    /**
     * @return the fabricStyle
     */
    public String getFabricStyle() {
        return fabricStyle;
    }

    /**
     * @return the count
     */
    public BigDecimal getCount() {
        return count;
    }

    /**
     * @return the dmcColor
     */
    public String getDmcColor() {
        return dmcColor;
    }

}
