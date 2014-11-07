/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.domain.entities.jpa;

import java.math.BigDecimal;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import org.hibernate.annotations.Type;
import org.joda.time.LocalDate;

/**
 *
 * @author tarasev
 */
@Entity(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @Column(name="id")
    private Long id;
    
    @Column(name="blocked", nullable = false)
    private Boolean blocked;
    
    @Column(name="price", nullable = false)
    private BigDecimal price;
    
    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentLocalDate")
    @Column(name="date", nullable = false)
    private LocalDate date;
    
    @Column(name="width")
    private Integer width;
    
    @Column(name="height")
    private Integer height;
    
    @Column(name="colors")
    private Integer colors;
    
    @Column(name="complexity")
    private Integer complexity;
    
    @ManyToOne
    @JoinColumn(name="canvas_id")
    private Canvas canvas;
    
    @ManyToOne
    @JoinColumn(name="thread_id")
    private Thread thread;
    
    @OneToMany
    @JoinColumn(name="product_id", referencedColumnName = "id")
    private List<File> files;
    
    @OneToOne
    @JoinColumn(name="text_field_name_id")
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
