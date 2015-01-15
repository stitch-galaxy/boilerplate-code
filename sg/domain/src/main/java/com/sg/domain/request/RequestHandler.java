/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.domain.request;

import com.sg.dto.request.cqrs.Request;
import com.sg.dto.request.response.cqrs.RequestResponse;
import java.lang.reflect.Type;

/**
 *
 * @author tarasev
 */
public interface RequestHandler {
    public RequestResponse handle(Request request);
    public Type getRequestType();
}
