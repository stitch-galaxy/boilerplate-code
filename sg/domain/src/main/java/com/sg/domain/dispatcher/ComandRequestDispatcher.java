/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.domain.dispatcher;

import com.sg.dto.command.cqrs.Command;
import com.sg.dto.command.response.cqrs.CommandResponse;
import com.sg.dto.request.cqrs.Request;
import com.sg.dto.request.response.cqrs.RequestResponse;

/**
 *
 * @author tarasev
 */
public interface ComandRequestDispatcher {
    public CommandResponse handle(Command command);
    public RequestResponse handle(Request request);
}
