/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.domain.operation;

import com.sg.dto.operation.Operation;
import com.sg.dto.operation.response.Response;

public interface OperationExecutor<TCommandResponse extends Response, TCommandType extends Operation> {
    public TCommandResponse handle(TCommandType command);
}
