/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.rest.utils;

import java.nio.charset.Charset;
import org.springframework.http.MediaType;

/**
 *
 * @author tarasev
 */
public class CustomMediaTypes {

    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf-8")
    );
}
