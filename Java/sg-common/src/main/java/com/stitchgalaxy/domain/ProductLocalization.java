/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.stitchgalaxy.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 *
 * @author tarasev
 */
public class ProductLocalization implements ValueObject<ProductLocalization> {

    private String locale;
    private String name;
    private String description;
    private String tags;

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
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the tags
     */
    public String getTags() {
        return tags;
    }

    /**
     * @param tags the tags to set
     */
    public void setTags(String tags) {
        this.tags = tags;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(locale)
                .append(name)
                .append(description)
                .append(tags)
                .toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        }

        ProductLocalization other = (ProductLocalization) obj;
        return sameValueAs(other);
    }

    public boolean sameValueAs(ProductLocalization other) {
        return other != null && new EqualsBuilder().
                append(this.locale, other.locale).
                append(this.name, other.name).
                append(this.description, other.description).
                append(this.tags, other.tags).
                isEquals();
    }

}
