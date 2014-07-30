/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.stitchgalaxy.domain.enumerations;

/**
 *
 * @author tarasev
 */
public enum TextType {

    PRODUCT_NAME(1);

    private int value;

    TextType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static TextType parse(int id) {
        TextType fileType = null; // Default
        for (TextType item : TextType.values()) {
            if (item.getValue() == id) {
                fileType = item;
                break;
            }
        }
        return fileType;
    }
}
