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
public class UnautohrizedAccessDto {

    private String message;

    private String requestUri;

    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * @return the requestUri
     */
    public String getRequestUri() {
        return requestUri;
    }

    /**
     * @param requestUri the requestUri to set
     */
    public void setRequestUri(String requestUri) {
        this.requestUri = requestUri;
    }
}
