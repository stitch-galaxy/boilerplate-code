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
public class FabricStyle {

    private final String style;

    public FabricStyle(String fabricStyle) {
        if (fabricStyle == null || fabricStyle.isEmpty()) {
            throw new IllegalArgumentException();
        }
        this.style = fabricStyle;
    }

    /**
     * @return the style
     */
    public String getFabricStyle() {
        return style;
    }

}
