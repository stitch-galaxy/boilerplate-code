/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.rest.dto;

/**
 *
 * @author tarasev
 */
public class AccessDenied {
    
    private final EventRef eventRef;
    
    public AccessDenied()
    {
        this.eventRef = new EventRef();
    }

    /**
     * @return the eventRef
     */
    public EventRef getEventRef() {
        return eventRef;
    }
}
