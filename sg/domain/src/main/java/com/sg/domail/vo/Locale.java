/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.domail.vo;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 *
 * @author tarasev
 */
public class Locale {

    private final String code;

    public Locale(String code) {
        if (code == null || code.isEmpty()) {
            throw new IllegalArgumentException();
        }
        this.code = code;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        }

        Locale other = (Locale) obj;
        return new EqualsBuilder().
                append(this.code, other.code).
                isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(code).build();
    }
}
