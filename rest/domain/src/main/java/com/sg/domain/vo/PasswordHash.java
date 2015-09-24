/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.domain.vo;

import org.apache.commons.lang.builder.EqualsBuilder;

/**
 *
 * @author Admin
 */
public class PasswordHash {

    private final String hash;

    public PasswordHash(String hash) {
        this.hash = hash;
    }

    /**
     * @return the hash
     */
    public String getHash() {
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        }

        PasswordHash other = (PasswordHash) obj;
        return new EqualsBuilder().
                append(this.hash, other.hash).
                isEquals();
    }
}
