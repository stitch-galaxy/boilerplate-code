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
public class Color {

    private final String colorCode;

    public Color(String colorCode) {
        if (colorCode == null || colorCode.isEmpty()) {
            throw new IllegalArgumentException();
        }
        this.colorCode = colorCode;
    }

    /**
     * @return the colorCode
     */
    public String getColorCode() {
        return colorCode;
    }
}
