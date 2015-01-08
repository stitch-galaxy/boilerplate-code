/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.domail.vo;

import java.net.MalformedURLException;
import java.net.URL;

/**
 *
 * @author tarasev
 */
public class FileUrl {
    private final URL url;
    
    public FileUrl(String url) throws MalformedURLException
    {
        this.url = new URL(url);
    }
    
    public String getUrl()
    {
        return url.toString();
    }
}
