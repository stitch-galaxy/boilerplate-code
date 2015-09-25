/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.domain.services;

import com.sg.domain.events.DomainEvent;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;

/**
 *
 * @author Admin
 */
@MessagingGateway
public interface DomainEventsRouter {

    public static final String INPUT_CHANNEL_NAME = "events.input";
    
    @Gateway(requestChannel = INPUT_CHANNEL_NAME)
    public void routeEvent(DomainEvent event);
}
