/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.stitchgalaxy.domain.entities.jpa;

import com.stitchgalaxy.domain.enumerations.TextFieldType;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

/**
 *
 * @author tarasev
 */
@Entity(name = "text_field")
public class TextField {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @Column(name="id")
    private Long id;

    @Column(name="text_field_type", nullable=false)
    private Integer textFieldType;
    
    public TextFieldType getTextFieldType()
    {
        return TextFieldType.parse(this.textFieldType);
    }
    
    public void setTextFieldType(TextFieldType textFieldType)
    {
        this.textFieldType = textFieldType.getValue();
    }
    
    @ManyToOne
    @JoinColumn(name="base_locale_id", nullable = false)
    private Locale baseLocale;
    
    @Column(name="base_value", nullable = false)
    private String baseValue;
    
    @Column(name="force_translation")
    private Boolean forceTranslation;
    
    @OneToMany
    @JoinColumn(name="text_field_id", referencedColumnName = "id")
    private List<TextFieldTranslation> translations;
}
