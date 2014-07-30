/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.stitchgalaxy.domain.entities.jpa;

import com.stitchgalaxy.domain.enumerations.TextType;

/**
 *
 * @author tarasev
 */
public class Text {
    private Long id;
    private TextType type;
    private Locale baseLocale;
    private String baseValue;
    private Boolean forceTranslation;
}
