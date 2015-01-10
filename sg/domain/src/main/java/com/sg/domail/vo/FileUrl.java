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

    public FileUrl(String url) {
        try {
            this.url = new URL(url);
        } catch (MalformedURLException ex) {
            throw new IllegalArgumentException(ex);
        }
    }

    public String getUrl() {
        return url.toString();
    }
}
