/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.rest.controllers;

import com.sg.rest.errorcodes.ErrorCodes;
import com.sg.dto.response.ErrorDto;
import com.sg.dto.response.ValidationErrorDto;
import com.sg.rest.utils.Utils;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 *
 * @author tarasev
 */
@ControllerAdvice
public class RestErrorHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestErrorHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ValidationErrorDto processValidationError(MethodArgumentNotValidException ex) {
        BindingResult result = ex.getBindingResult();
        List<FieldError> errors = result.getFieldErrors();
        Set<String> fieldErrors = new HashSet<String>();
        for (FieldError error : errors) {
            fieldErrors.add(error.getDefaultMessage());
        }
        return new ValidationErrorDto(fieldErrors);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public void processServiceValidationError(HttpRequestMethodNotSupportedException ex) {
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ErrorDto processException(ConstraintViolationException ex) throws Exception {
        String refNumber = Utils.logException(LOGGER, ex);
        ErrorDto dto = new ErrorDto();
        dto.setRefNumber(refNumber);

        StringBuilder sb = new StringBuilder();
        sb.append(ex.getMessage());
        sb.append(": ");

        boolean first = true;
        for (ConstraintViolation violation : ex.getConstraintViolations()) {
            if (first) {
                first = false;
            } else {
                sb.append(", ");
            }
            sb.append(violation.getMessage());
        }
        dto.setError(sb.toString());
        dto.setErrorCode(ErrorCodes.EXCEPTION_CONSTRAINT_VIOLATION);
        return dto;
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ErrorDto processException(Exception ex) throws Exception {
        String refNumber = Utils.logException(LOGGER, ex);

        if (AnnotationUtils.findAnnotation(ex.getClass(), ResponseStatus.class) != null) {
            throw ex;
        }

        ErrorDto dto = new ErrorDto();
        dto.setRefNumber(refNumber);
        dto.setError(ex.getMessage());
        dto.setErrorCode(ErrorCodes.EXCEPTION);
        return dto;
    }
}
