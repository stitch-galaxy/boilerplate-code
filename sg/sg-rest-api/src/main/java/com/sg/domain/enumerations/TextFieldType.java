/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.domain.enumerations;

/**
 *
 * @author tarasev
 */
public enum TextFieldType {

    PRODUCT_NAME(1);

    private int value;

    TextFieldType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static TextFieldType parse(int id) {
        TextFieldType fileType = null; // Default
        for (TextFieldType item : TextFieldType.values()) {
            if (item.getValue() == id) {
                fileType = item;
                break;
            }
        }
        return fileType;
    }
}
