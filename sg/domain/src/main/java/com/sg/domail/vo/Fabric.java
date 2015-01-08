/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.domail.vo;

import java.math.BigDecimal;

/**
 *
 * @author tarasev
 */
//http://www.123stitch.com/cgi-perl/fabric.pl
public class Fabric {

    private final String fabricStyle;
    private final BigDecimal count;
    private final String dmcColor;

    public Fabric(String fabricStyle, BigDecimal count, String dmcColor) {

        this.fabricStyle = fabricStyle;
        this.count = count;
        this.dmcColor = dmcColor;
        verifyData();
    }

    private void verifyData() {
        if (fabricStyle == null
                || count == null
                || dmcColor == null) {
            throw new IllegalArgumentException();
        }

        if (fabricStyle.isEmpty() || dmcColor.isEmpty()) {
            throw new IllegalArgumentException();
        }

        if (count.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException();
        }
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
