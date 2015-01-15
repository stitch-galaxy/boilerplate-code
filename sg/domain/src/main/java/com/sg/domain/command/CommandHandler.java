/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.domain.command;

import com.sg.dto.command.cqrs.Command;
import com.sg.dto.command.response.cqrs.CommandResponse;
import java.lang.reflect.Type;

/**
 *
 * @author tarasev
 */
public interface CommandHandler {
    public CommandResponse handle(Command command);
    public Type getCommandType();
}
