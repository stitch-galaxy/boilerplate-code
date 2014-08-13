/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.sg_rest_api.utils;

import com.sg.dto.ErrorDto;
import java.util.UUID;
import org.slf4j.Logger;

/**
 *
 * @author tarasev
 */
public class Utils {

    public static ErrorDto logExceptionAndCreateErrorDto(Logger LOGGER, Exception ex) {
        String refNumber = UUID.randomUUID().toString().toUpperCase();
        StringBuilder sb = new StringBuilder();
        sb.append("Exception refNumber: ");
        sb.append(refNumber);
        LOGGER.error(sb.toString(), ex);

        ErrorDto dto = new ErrorDto();
        dto.setError(ex.getMessage());
        dto.setRefNumber(refNumber);

        return dto;
    }

}
