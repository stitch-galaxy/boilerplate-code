/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.infrastructure;

import com.sg.domain.events.DomainEvent;
import com.sg.domain.services.DomainEventsRouter;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;

/**
 *
 * @author Admin
 */
@MessagingGateway
public interface DomainEventsRouterService extends DomainEventsRouter {

    public static final String INPUT_CHANNEL_NAME = "events.input";
    
    @Gateway(requestChannel = INPUT_CHANNEL_NAME)
    @Override
    public void routeEvent(DomainEvent event);
}
