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
import org.springframework.integration.router.PayloadTypeRouter;

/**
 *
 * @author Admin
 */
@EnableIntegration
@IntegrationComponentScan(basePackageClasses = {InfrastructureNoOp.class})
public class DomainEventsRoutingConfig {
    
    @Autowired
    private AccountRegisteredEventHandler accountRegisteredEventHandler;

    @Bean PayloadTypeRouter payloadTypeRouter()
    {
        PayloadTypeRouter router = new PayloadTypeRouter();
        router.setChannelMapping(AccountRegisteredEvent.class.getName(), AccountRegisteredEventHandler.class.getName());
        return router;
    }
    
    @Bean
    public IntegrationFlow myFlow() {
        
        //return IntegrationFlows.from(DomainEventsRouterService.INPUT_CHANNEL_NAME).route(AccountRegisteredEvent.class, p -> p).channel(MessageChannels.publishSubscribe()).handle(m -> accountRegisteredEventHandler.processEvent((AccountRegisteredEvent) m.getPayload())).get();
        return IntegrationFlows.from(DomainEventsRouterService.INPUT_CHANNEL_NAME).route(payloadTypeRouter()).get();
    }
    
    @Bean
    public IntegrationFlow AccountRegisteredEventHandlerFlow() {
        return IntegrationFlows.from(AccountRegisteredEventHandler.class.getName()).channel(MessageChannels.publishSubscribe()).handle(m -> accountRegisteredEventHandler.processEvent((AccountRegisteredEvent) m.getPayload())).get();
    }
    
}
