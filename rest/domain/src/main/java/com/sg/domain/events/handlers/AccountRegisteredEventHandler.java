/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.domain.events.handlers;

import com.sg.domain.events.AccountRegisteredEvent;
import org.springframework.integration.annotation.MessageEndpoint;

/**
 *
 * @author Admin
 */
@MessageEndpoint
public class AccountRegisteredEventHandler {

    public void processEvent(AccountRegisteredEvent event) {
    }

}
