/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sg.dto.response;

import java.util.HashSet;
import java.util.Set;
import org.apache.commons.lang3.builder.EqualsBuilder;

/**
 *
 * @author tarasev
 */
public class ValidationErrorDto {
    private Set<String> fieldErrors;
    
    public ValidationErrorDto(Set<String> fieldErrors)
    {
        this.fieldErrors = fieldErrors;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        }

        ValidationErrorDto other = (ValidationErrorDto) obj;
        return new EqualsBuilder().
                append(this.getFieldErrors(), other.getFieldErrors()).
                isEquals();
    }

    /**
     * @return the fieldErrors
     */
    public Set<String> getFieldErrors() {
        return fieldErrors;
    }

    /**
     * @param fieldErrors the fieldErrors to set
     */
    public void setFieldErrors(Set<String> fieldErrors) {
        this.fieldErrors = fieldErrors;
    }
}
