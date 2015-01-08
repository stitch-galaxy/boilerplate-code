/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.domail.vo;

import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author tarasev
 */
public class TextField {
    private final Set<TextFieldTranslation> translations;
    public TextField(Set<TextFieldTranslation> translations)
    {
        this.translations = translations;
        verifyData();
    }
    
    public TextField setTranslation(TextFieldTranslation translation)
    {
        Set<TextFieldTranslation> newTranslations = new HashSet<TextFieldTranslation>();
        newTranslations.addAll(translations);
        newTranslations.add(translation);
        return new TextField(newTranslations);
    }
    
    private void verifyData()
    {
        if (translations == null)
            throw new IllegalArgumentException();
    }
}
