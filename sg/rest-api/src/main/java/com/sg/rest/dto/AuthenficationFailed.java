/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.rest.dto;

import com.sg.rest.enumerations.AuthentificationFailureStatus;

/**
 *
 * @author tarasev
 */
public class AuthenficationFailed {
    
    private final EventRef eventRef;
    private final AuthentificationFailureStatus status;
    
    public AuthenficationFailed(AuthentificationFailureStatus status)
    {
        this.eventRef = new EventRef();
        this.status = status;
    }

    /**
     * @return the eventRef
     */
    public EventRef getEventRef() {
        return eventRef;
    }

    /**
     * @return the status
     */
    public AuthentificationFailureStatus getStatus() {
        return status;
    }
}
