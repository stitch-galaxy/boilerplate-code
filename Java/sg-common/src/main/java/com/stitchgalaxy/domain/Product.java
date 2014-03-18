/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.stitchgalaxy.domain;

import java.awt.Color;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.joda.time.LocalDate;

/**
 *
 * @author tarasev
 */
public class Product implements Entity<Product> {

    private String name;
    private Long id;
    private Boolean blocked;
    private String description;
    private LocalDate date;
    private BigDecimal priceUsd;
    private Partner author;
    private Partner translator;
    private Long sales;
    private Long rating;
    private Long rates;
    private Integer avgColorRed;
    private Integer avgColorGreen;
    private Integer avgColorBlue;

    private Integer width;
    private Integer height;
    private Integer colors;
    private String canvas;
    private String threads;
    private BigDecimal stitchesPerInch;

    private Integer complexity;
    private String tags;
    private final Set<ProductLocalization> localizations = new HashSet<ProductLocalization>();
    private final Set<Category> categories = new HashSet<Category>();
    private String prototypeUri;
    private String thumbnailUri;
    private String largeImageUri;
    private String completeProductUri;
    private String fileUri;

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
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the blocked
     */
    public Boolean isBlocked() {
        return blocked;
    }

    public boolean getBlocked() {
        return blocked != null && blocked;
    }

    /**
     * @param blocked the blocked to set
     */
    public void setBlocked(Boolean blocked) {
        this.blocked = blocked;
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
     * @return the date
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * @param date the date to set
     */
    public void setDate(LocalDate date) {
        this.date = date;
    }

    /**
     * @return the priceUsd
     */
    public BigDecimal getPriceUsd() {
        return priceUsd;
    }

    /**
     * @param priceUsd the priceUsd to set
     */
    public void setPriceUsd(BigDecimal priceUsd) {
        this.priceUsd = priceUsd;
    }

    /**
     * @return the author
     */
    public Partner getAuthor() {
        return author;
    }

    /**
     * @param author the author to set
     */
    public void setAuthor(Partner author) {
        this.author = author;
    }

    /**
     * @return the translator
     */
    public Partner getTranslator() {
        return translator;
    }

    /**
     * @param translator the translator to set
     */
    public void setTranslator(Partner translator) {
        this.translator = translator;
    }

    /**
     * @return the sales
     */
    public Long getSales() {
        return sales;
    }

    /**
     * @param sales the sales to set
     */
    public void setSales(Long sales) {
        this.sales = sales;
    }

    /**
     * @return the rating
     */
    public Long getRating() {
        return rating;
    }

    /**
     * @param rating the rating to set
     */
    public void setRating(Long rating) {
        this.rating = rating;
    }

    /**
     * @return the rates
     */
    public Long getRates() {
        return rates;
    }

    /**
     * @param rates the rates to set
     */
    public void setRates(Long rates) {
        this.rates = rates;
    }

    /**
     * @return the avgColor
     */
    public Color getAvgColor() {
        return new Color(getAvgColorRed(), getAvgColorGreen(), getAvgColorBlue());
    }

    /**
     * @param avgColor the avgColor to set
     */
    public void setAvgColor(Color avgColor) {
        this.setAvgColorRed((Integer) avgColor.getRed());
        this.setAvgColorGreen((Integer) avgColor.getGreen());
        this.setAvgColorBlue((Integer) avgColor.getBlue());
    }

    /**
     * @return the complexity
     */
    public Integer getComplexity() {
        return complexity;
    }

    /**
     * @param complexity the complexity to set
     */
    public void setComplexity(Integer complexity) {
        this.complexity = complexity;
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

    /**
     * @return the localizations
     */
    public Set<ProductLocalization> getLocalizations() {
        return localizations;
    }

    /**
     * @return the prototypeUri
     */
    public String getPrototypeUri() {
        return prototypeUri;
    }

    /**
     * @param prototypeUri the prototypeUri to set
     */
    public void setPrototypeUri(String prototypeUri) {
        this.prototypeUri = prototypeUri;
    }

    /**
     * @return the thumbnailUri
     */
    public String getThumbnailUri() {
        return thumbnailUri;
    }

    /**
     * @param thumbnailUri the thumbnailUri to set
     */
    public void setThumbnailUri(String thumbnailUri) {
        this.thumbnailUri = thumbnailUri;
    }

    /**
     * @return the largeImageUri
     */
    public String getLargeImageUri() {
        return largeImageUri;
    }

    /**
     * @param largeImageUri the largeImageUri to set
     */
    public void setLargeImageUri(String largeImageUri) {
        this.largeImageUri = largeImageUri;
    }

    /**
     * @return the completeImageUri
     */
    public String getCompleteProductUri() {
        return completeProductUri;
    }

    /**
     * @param completeImageUri the completeImageUri to set
     */
    public void setCompleteProductUri(String completeProductUri) {
        this.completeProductUri = completeProductUri;
    }

    /**
     * @return the categories
     */
    public Set<Category> getCategories() {
        return categories;
    }

    /**
     * @return the avgColorRed
     */
    public Integer getAvgColorRed() {
        return avgColorRed;
    }

    /**
     * @param avgColorRed the avgColorRed to set
     */
    public void setAvgColorRed(Integer avgColorRed) {
        this.avgColorRed = avgColorRed;
    }

    /**
     * @return the avgColorGreen
     */
    public Integer getAvgColorGreen() {
        return avgColorGreen;
    }

    /**
     * @param avgColorGreen the avgColorGreen to set
     */
    public void setAvgColorGreen(Integer avgColorGreen) {
        this.avgColorGreen = avgColorGreen;
    }

    /**
     * @return the avgColorBlue
     */
    public Integer getAvgColorBlue() {
        return avgColorBlue;
    }

    /**
     * @param avgColorBlue the avgColorBlue to set
     */
    public void setAvgColorBlue(Integer avgColorBlue) {
        this.avgColorBlue = avgColorBlue;
    }

    /**
     * @return the width
     */
    public Integer getWidth() {
        return width;
    }

    /**
     * @param width the width to set
     */
    public void setWidth(Integer width) {
        this.width = width;
    }

    /**
     * @return the height
     */
    public Integer getHeight() {
        return height;
    }

    /**
     * @param height the height to set
     */
    public void setHeight(Integer height) {
        this.height = height;
    }

    /**
     * @return the colors
     */
    public Integer getColors() {
        return colors;
    }

    /**
     * @param colors the colors to set
     */
    public void setColors(Integer colors) {
        this.colors = colors;
    }

    /**
     * @return the canvas
     */
    public String getCanvas() {
        return canvas;
    }

    /**
     * @param canvas the canvas to set
     */
    public void setCanvas(String canvas) {
        this.canvas = canvas;
    }

    /**
     * @return the threads
     */
    public String getThreads() {
        return threads;
    }

    /**
     * @param threads the threads to set
     */
    public void setThreads(String threads) {
        this.threads = threads;
    }

    /**
     * @return the stitchesPerInch
     */
    public BigDecimal getStitchesPerInch() {
        return stitchesPerInch;
    }

    /**
     * @param stitchesPerInch the stitchesPerInch to set
     */
    public void setStitchesPerInch(BigDecimal stitchesPerInch) {
        this.stitchesPerInch = stitchesPerInch;
    }

    /**
     * @return the fileUri
     */
    public String getFileUri() {
        return fileUri;
    }

    /**
     * @param fileUri the fileUri to set
     */
    public void setFileUri(String fileUri) {
        this.fileUri = fileUri;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(id)
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

        Product other = (Product) obj;
        return sameIdentityAs(other);
    }

    public boolean sameIdentityAs(Product other) {
        return other != null && new EqualsBuilder().
                append(this.id, other.id).
                isEquals();
    }
}
