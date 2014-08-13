/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.dto;

import org.apache.commons.lang3.builder.EqualsBuilder;

/**
 *
 * @author tarasev
 */
public class CanvasUpdateDto {

    private CanvasRefDto ref;
    private CanvasDto dto;

    /**
     * @return the ref
     */
    public CanvasRefDto getRef() {
        return ref;
    }

    /**
     * @param ref the ref to set
     */
    public void setRef(CanvasRefDto ref) {
        this.ref = ref;
    }

    /**
     * @return the dto
     */
    public CanvasDto getDto() {
        return dto;
    }

    /**
     * @param dto the dto to set
     */
    public void setDto(CanvasDto dto) {
        this.dto = dto;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        }

        CanvasUpdateDto other = (CanvasUpdateDto) obj;
        return new EqualsBuilder().
                append(this.dto, other.dto).
                append(this.ref, other.ref).
                isEquals();
    }

}
