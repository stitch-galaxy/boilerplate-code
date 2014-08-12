/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sg.dto;

/**
 *
 * @author tarasev
 */
public class ThreadUpdateDto {
    
    private ThreadRefDto ref;
    private ThreadDto dto;

    /**
     * @return the ref
     */
    public ThreadRefDto getRef() {
        return ref;
    }

    /**
     * @param ref the ref to set
     */
    public void setRef(ThreadRefDto ref) {
        this.ref = ref;
    }

    /**
     * @return the dto
     */
    public ThreadDto getDto() {
        return dto;
    }

    /**
     * @param dto the dto to set
     */
    public void setDto(ThreadDto dto) {
        this.dto = dto;
    }
    
}
