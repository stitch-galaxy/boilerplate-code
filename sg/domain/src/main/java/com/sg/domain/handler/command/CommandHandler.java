/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.domain.handler.command;

import com.sg.dto.command.cqrs.Command;
import com.sg.dto.command.response.CommandResponse;

public interface CommandHandler<TCommandResponse extends CommandResponse, TCommandType extends Command> {
    public TCommandResponse handle(TCommandType command);
}
