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
public class TextTranslation {
    private Text text;
    private Locale locale;
    private String value;
}
