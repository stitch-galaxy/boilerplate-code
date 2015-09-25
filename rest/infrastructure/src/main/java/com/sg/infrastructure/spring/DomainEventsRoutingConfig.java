/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.infrastructure.spring;

import com.sg.domain.events.AccountRegisteredEvent;
import com.sg.domain.events.handlers.AccountRegisteredEventHandler;
import com.sg.infrastructure.DomainEventsRouterService;
import com.sg.infrastructure.InfrastructureNoOp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.channel.MessageChannels;

/**
 *
 * @author Admin
 */
@EnableIntegration
@IntegrationComponentScan(basePackageClasses = {InfrastructureNoOp.class})
public class DomainEventsRoutingConfig {
    
    @Autowired
    private AccountRegisteredEventHandler accountRegisteredEventHandler;

    @Bean
    public IntegrationFlow myFlow() {
        return IntegrationFlows.from(DomainEventsRouterService.INPUT_CHANNEL_NAME).route(AccountRegisteredEvent.class, p -> p).channel(MessageChannels.publishSubscribe()).handle(m -> accountRegisteredEventHandler.processEvent((AccountRegisteredEvent) m.getPayload())).get();
    }
    
}
