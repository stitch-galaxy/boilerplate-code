/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.domain.handler.request;

import com.sg.dto.request.cqrs.Request;
import com.sg.dto.request.response.RequestResponse;

/**
 *
 * @author tarasev
 */
public interface RequestHandler<TRequestResponse extends RequestResponse,TRequest extends Request> {
    public TRequestResponse handle(TRequest request);
}
