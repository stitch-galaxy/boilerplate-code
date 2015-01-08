/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.domail.vo;

/**
 *
 * @author tarasev
 */
public class Locale {

    private final String code;

    public Locale(String code) {
        this.code = code;
        verifyData();
    }
    
    private void verifyData() {
        if (code == null
                || code.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }
}
