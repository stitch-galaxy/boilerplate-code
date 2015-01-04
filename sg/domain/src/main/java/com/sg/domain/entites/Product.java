/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.domain.entites;

import java.math.BigDecimal;
import java.util.List;
import org.joda.time.LocalDate;

/**
 *
 * @author tarasev
 */
public class Product {
    private Long id;
    private Boolean blocked;
    private BigDecimal price;
    private LocalDate date;
    private Integer width;
    private Integer height;
    private Integer colors;
    private Integer complexity;
    private Canvas canvas;
    private Thread thread;
    private List<File> files;
    private TextField name;

    /**
     * @return the price
     */
    public BigDecimal getPrice() {
        return price;
    }

    /**
     * @param price the price to set
     */
    public void setPrice(BigDecimal price) {
        this.price = price;
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
     * @return the blocked
     */
    public Boolean getBlocked() {
        return blocked;
    }

    /**
     * @param blocked the blocked to set
     */
    public void setBlocked(Boolean blocked) {
        this.blocked = blocked;
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
}
