/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.domain.services;

import com.sg.domain.events.DomainEvent;

/**
 *
 * @author Admin
 */
public interface DomainEventsRouter {

    public void routeEvent(DomainEvent event);
}
