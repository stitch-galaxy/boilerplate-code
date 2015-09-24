package com.sg.domain.authtoken;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author tarasev
 */
public abstract class BaseTokenException extends Exception {

    public BaseTokenException(Throwable cause) {
        super(cause);
    }
}