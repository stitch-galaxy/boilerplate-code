/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sg.domail.vo;

/**
 *
 * @author tarasev
 */
public class TextFieldTranslation {
    private final Locale locale;
    private final String text;
    
    public TextFieldTranslation(Locale locale, String text)
    {
        this.locale = locale;
        this.text = text;
        verifyData();
    }
    
    private void verifyData()
    {
        if (locale == null || text == null)
            throw new IllegalArgumentException();
    }

    /**
     * @return the locale
     */
    public Locale getLocale() {
        return locale;
    }

    /**
     * @return the text
     */
    public String getText() {
        return text;
    }
}
