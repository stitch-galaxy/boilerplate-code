/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.dto.operation.response;

import com.sg.dto.operation.status.CompleteSignupCommandStatus;

/**
 *
 * @author tarasev
 */
public class CompleteSignupResponse implements Response {

    /**
     * @return the data
     */
    public Data getData() {
        return data;
    }

    public static class Data {

        private final String token;

        public Data(String token) {
            if (token == null) {
                throw new IllegalArgumentException();
            }
            this.token = token;
        }

        /**
         * @return the token
         */
        public String getToken() {
            return token;
        }

    }

    private final CompleteSignupCommandStatus status;
    private final Data data;

    public CompleteSignupResponse(CompleteSignupCommandStatus status) {
        if (status == null || status == CompleteSignupCommandStatus.STATUS_SUCCESS) {
            throw new IllegalArgumentException();
        }
        this.status = status;
        this.data = null;
    }

    public CompleteSignupResponse(String token) {
        this.status = CompleteSignupCommandStatus.STATUS_SUCCESS;
        this.data = new Data(token);
    }

    /**
     * @return the status
     */
    public CompleteSignupCommandStatus getStatus() {
        return status;
    }

}
