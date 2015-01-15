/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.domain.dispatcher;

import com.sg.domain.command.CommandHandler;
import com.sg.domain.request.RequestHandler;
import com.sg.dto.command.cqrs.Command;
import com.sg.dto.command.response.cqrs.CommandResponse;
import com.sg.dto.request.cqrs.Request;
import com.sg.dto.request.response.cqrs.RequestResponse;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author tarasev
 */
public class ComandRequestDispatcherImpl implements ComandRequestDispatcher {

    private final Map<String, CommandHandler> commandHandlers;
    private final Map<String, RequestHandler> requestHandlers;

    public ComandRequestDispatcherImpl() {
        this.commandHandlers = new HashMap<String, CommandHandler>();
        this.requestHandlers = new HashMap<String, RequestHandler>();
    }

    public void RegisterComandHandler(CommandHandler handler) {
        if (handler == null) {
            throw new IllegalArgumentException();
        }
        String typeName = handler.getCommandType().getTypeName();
        if (commandHandlers.containsKey(typeName)) {
            throw new IllegalStateException("Handler for " + typeName + " type already registered");
        }
        commandHandlers.put(typeName, handler);
    }
    
    public void RegisterRequestHandler(RequestHandler handler) {
        if (handler == null) {
            throw new IllegalArgumentException();
        }
        String typeName = handler.getRequestType().getTypeName();
        if (requestHandlers.containsKey(typeName)) {
            throw new IllegalStateException("Handler for " + typeName + " type already registered");
        }
        requestHandlers.put(typeName, handler);
    }

    @Override
    public CommandResponse handle(Command command) {
        String typeName = command.getClass().getGenericSuperclass().getTypeName();
        if (commandHandlers.containsKey(typeName))
        {
            return commandHandlers.get(typeName).handle(command);
        }
        else
        {
            throw new IllegalArgumentException("Handler for " + typeName + " type not registered");
        }
    }

    @Override
    public RequestResponse handle(Request request) {
        String typeName = request.getClass().getGenericSuperclass().getTypeName();
        if (requestHandlers.containsKey(typeName))
        {
            return requestHandlers.get(typeName).handle(request);
        }
        else
        {
            throw new IllegalArgumentException("Handler for " + typeName + " type not registered");
        }
    }

}
