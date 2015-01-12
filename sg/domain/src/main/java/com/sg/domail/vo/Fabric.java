/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.domail.vo;

/**
 *
 * @author tarasev
 */
//http://www.123stitch.com/cgi-perl/fabric.pl
public class Fabric {

    private final FabricStyle style;
    private final FabricCount count;
    private final FabricColor color;

    public Fabric(FabricStyle style, FabricCount count, FabricColor color) {
        if (style == null || count == null || color == null) {
            throw new IllegalArgumentException();
        }
        this.style = style;
        this.color = color;
        this.count = count;
    }

    /**
     * @return the style
     */
    public FabricStyle getStyle() {
        return style;
    }

    /**
     * @return the count
     */
    public FabricCount getCount() {
        return count;
    }

    /**
     * @return the color
     */
    public FabricColor getColor() {
        return color;
    }
}
