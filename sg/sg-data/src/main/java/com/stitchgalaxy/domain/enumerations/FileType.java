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
public enum FileType {

    PROTOTYPE_FULL_SIZE(1),
    DESIGN_FULL_SIZE(2),
    DESIGN_THUMBNAIL(3),
    COMPLETE_DESIGN_FULL_SIZE(4),
    COMPLETE_DESIGN_THUMBNAIL(5),
    PDF(6),
    BINARY(7),
    JSON(8);

    private int value;

    FileType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static FileType parse(int id) {
        FileType fileType = null; // Default
        for (FileType item : FileType.values()) {
            if (item.getValue() == id) {
                fileType = item;
                break;
            }
        }
        return fileType;
    }
}
