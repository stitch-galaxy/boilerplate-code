/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sg.domain.entites;

/**
 *
 * @author tarasev
 */
public class Locale {
    private Long id;
    private String code;
    private String englishName;
    private String localName;
    private String locale;
    private String region;
    private Boolean translateTo;
    private Boolean isActive;

    /**
     * @return the code
     */
    public String getCode() {
        return code;
    }

    /**
     * @param locale the code to set
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * @return the englishName
     */
    public String getEnglishName() {
        return englishName;
    }

    /**
     * @param englishName the englishName to set
     */
    public void setEnglishName(String englishName) {
        this.englishName = englishName;
    }

    /**
     * @return the localName
     */
    public String getLocalName() {
        return localName;
    }

    /**
     * @param localName the localName to set
     */
    public void setLocalName(String localName) {
        this.localName = localName;
    }

    /**
     * @return the locale
     */
    public String getLocale() {
        return locale;
    }

    /**
     * @param locale the locale to set
     */
    public void setLocale(String locale) {
        this.locale = locale;
    }

    /**
     * @return the region
     */
    public String getRegion() {
        return region;
    }

    /**
     * @param region the region to set
     */
    public void setRegion(String region) {
        this.region = region;
    }

    /**
     * @return the translateTo
     */
    public Boolean getTranslateTo() {
        return translateTo;
    }

    /**
     * @param translateTo the translateTo to set
     */
    public void setTranslateTo(Boolean translateTo) {
        this.translateTo = translateTo;
    }

    /**
     * @return the isActive
     */
    public Boolean getIsActive() {
        return isActive;
    }

    /**
     * @param isActive the isActive to set
     */
    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
}