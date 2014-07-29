/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.stitchgalaxy.domain.entities.jpa;

import java.math.BigDecimal;
import javax.annotation.Generated;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import org.joda.time.LocalDate;

/**
 *
 * @author tarasev
 */
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;
    private String name;
    private Boolean blocked;
    private BigDecimal price;
    private LocalDate date;
    
    private Integer width;
    private Integer height;
    private Integer colors;
    
    private String canvas;
    private BigDecimal stitchesPerInch;
    private String threads;
    
    private Integer complexity;
    
    private String prototypeUrl;
    private String thumbnailUrl;
    private String imageUrl;
    private String completeProductUrl;
    private String fileUrl;
}
