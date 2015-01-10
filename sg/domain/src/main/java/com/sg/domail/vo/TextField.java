/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.domail.vo;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author tarasev
 */
public class TextField {
    private final Map<Locale, String> translations;
    public TextField(Map<Locale, String> translations)
    {
        if (translations == null)
            throw new IllegalArgumentException();
        this.translations = translations;
    }
    
    public TextField setTranslation(Locale locale, String text)
    {
        if (locale == null || text == null)
            throw new IllegalArgumentException();
        Map<Locale, String> newTranslations = new HashMap<Locale, String>();
        newTranslations.putAll(translations);
        newTranslations.put(locale, text);
        return new TextField(newTranslations);
    }
}
