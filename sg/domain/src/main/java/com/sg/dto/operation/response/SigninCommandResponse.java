/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.dto.operation.response;

import com.sg.dto.operation.status.SigninStatus;

/**
 *
 * @author tarasev
 */
public class SigninCommandResponse implements Response {

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

    private final SigninStatus status;
    private final Data data;

    public SigninCommandResponse(SigninStatus status) {
        if (status == null || status == SigninStatus.STATUS_SUCCESS) {
            throw new IllegalArgumentException();
        }
        this.status = status;
        this.data = null;
    }

    public SigninCommandResponse(String token) {
        if (token == null) {
            throw new IllegalArgumentException();
        }
        this.status = SigninStatus.STATUS_SUCCESS;
        this.data = new Data(token);
    }

    /**
     * @return the status
     */
    public SigninStatus getStatus() {
        return status;
    }

}
