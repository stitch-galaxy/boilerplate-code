/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sg.sg_rest_api.dto;

import org.codehaus.jackson.annotate.JsonIgnore;

/**
 *
 * @author tarasev
 */
public class ErrorDto {
    
    @JsonIgnore
    private Exception exception;
    
    private String message;
    
    @JsonIgnore
    private String exceptionMessage;
    
    @JsonIgnore
    private String stackTrace;

    /**
     * @return the exception
     */
    public Exception getException() {
        return exception;
    }

    /**
     * @param exception the exception to set
     */
    public void setException(Exception exception) {
        this.exception = exception;
    }

    /**
     * @return the exceptionMessage
     */
    public String getExceptionMessage() {
        return exceptionMessage;
    }

    /**
     * @param exceptionMessage the exceptionMessage to set
     */
    public void setExceptionMessage(String exceptionMessage) {
        this.exceptionMessage = exceptionMessage;
    }

    /**
     * @return the stackTrace
     */
    public String getStackTrace() {
        return stackTrace;
    }

    /**
     * @param stackTrace the stackTrace to set
     */
    public void setStackTrace(String stackTrace) {
        this.stackTrace = stackTrace;
    }

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
    
}
