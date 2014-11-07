/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sg.domain.entities.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 *
 * @author tarasev
 */
@Entity(name = "text_field_translation")
public class TextFieldTranslation {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @Column(name="id")
    private Long id;
    
    @ManyToOne
    @JoinColumn(name="locale_id", nullable = false)
    private Locale locale;
    
    @Column(name="value", nullable = false)
    private String value;
}
