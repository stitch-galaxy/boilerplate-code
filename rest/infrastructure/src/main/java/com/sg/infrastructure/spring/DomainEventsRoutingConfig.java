/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.infrastructure.spring;

import com.sg.domain.events.AccountRegistrationEvent;
import com.sg.domain.events.ResendRegistrationConfirmationEmailEvent;
import com.sg.domain.events.handlers.AccountRelatedEventsHandler;
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
    private AccountRelatedEventsHandler accountRelatedEventsHandler;

    @Bean PayloadTypeRouter payloadTypeRouter()
    {
        PayloadTypeRouter router = new PayloadTypeRouter();
        router.setChannelMapping(AccountRegistrationEvent.class.getName(), AccountRegistrationEvent.class.getName());
        router.setChannelMapping(ResendRegistrationConfirmationEmailEvent.class.getName(), ResendRegistrationConfirmationEmailEvent.class.getName());
        return router;
    }
    
    @Bean
    public IntegrationFlow myFlow() {
        
        return IntegrationFlows.from(DomainEventsRouterService.INPUT_CHANNEL_NAME).route(payloadTypeRouter()).get();
    }
    
    @Bean
    public IntegrationFlow AccountRegistrationEventFlow() {
        return IntegrationFlows.from(MessageChannels.publishSubscribe(AccountRegistrationEvent.class.getName())).handle(m -> accountRelatedEventsHandler.processEvent((AccountRegistrationEvent) m.getPayload())).get();
    }
    
    @Bean
    public IntegrationFlow ResendRegistrationConfirmationEmailEventFlow() {
        return IntegrationFlows.from(MessageChannels.publishSubscribe(ResendRegistrationConfirmationEmailEvent.class.getName())).handle(m -> accountRelatedEventsHandler.processEvent((ResendRegistrationConfirmationEmailEvent) m.getPayload())).get();
    }
    
}
