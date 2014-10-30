/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.constants;

import com.sg.dto.request.CanvasCreateDto;

/**
 *
 * @author tarasev
 */
public enum DtoFields {

    FIELD_CREATE_CANVAS_DTO_CODE(DtoFieldCodes.FIELD_CREATE_CANVAS_DTO_CODE, CanvasCreateDto.class);

    private final String fieldName;
    private final Class dtoClass;

    private DtoFields(String fieldName, Class dtoClass) {
        this.fieldName = fieldName;
        this.dtoClass = dtoClass;
    }

    /**
     * @return the fieldName
     */
    public String getFieldName() {
        return fieldName;
    }

    /**
     * @return the dtoClass
     */
    public Class getDtoClass() {
        return dtoClass;
    }
}
