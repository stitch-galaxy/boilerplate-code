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
public enum TokenType {

    WEB_SESSION_TOKEN(1),
    EMAIL_VERIFICATION_TOKEN(2),
    PASWORD_RESET_TOKEN(3);

    private final int id;

    TokenType(int id) {
        this.id = id;
    }

    public static TokenType parse(int id) {
        for (TokenType e : TokenType.values()) {
            if (e.getId() == id) {
                return e;
            }
        }
        return null;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

}
