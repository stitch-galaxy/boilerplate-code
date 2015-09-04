/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.domain.vo;

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
}
