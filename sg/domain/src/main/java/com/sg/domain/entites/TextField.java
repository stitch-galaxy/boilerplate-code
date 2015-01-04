/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sg.domain.entites;

import com.sg.domain.enumerations.TextFieldType;
import java.util.List;

/**
 *
 * @author tarasev
 */
public class TextField {
    private Long id;
    private Integer textFieldType;
    private Locale baseLocale;
    private String baseValue;
    private Boolean forceTranslation;
    private List<TextFieldTranslation> translations;

    
    public TextFieldType getTextFieldType()
    {
        return TextFieldType.parse(this.textFieldType);
    }
    
    public void setTextFieldType(TextFieldType textFieldType)
    {
        this.textFieldType = textFieldType.getValue();
    }
    
}
