/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.rest.enumerations;

/**
 *
 * @author tarasev
 */
public enum HttpCustomHeaders {

    AUTH_TOKEN_HEADER("X-Auth-Token");

    private final String header;

    HttpCustomHeaders(String header) {
        this.header = header;
    }

    /**
     * @return the header
     */
    public String getHeader() {
        return header;
    }

}
