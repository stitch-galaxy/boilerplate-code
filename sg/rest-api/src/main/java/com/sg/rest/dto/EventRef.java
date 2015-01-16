/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.rest.dto;

import java.util.UUID;

/**
 *
 * @author tarasev
 */
public class EventRef {
    
    private final UUID uuid;
    
    public EventRef()
    {
        this.uuid = UUID.randomUUID();
    }
    
    public String getId()
    {
        return this.uuid.toString().toUpperCase();
    }
}
