/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.stitchgalaxy.sg_manager_web.data;

import java.awt.Color;
import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;
import org.joda.time.LocalDate;

/**
 *
 * @author tarasev
 */
public class Product {
    private String name;
    private UUID uuid;
        private boolean blocked;
        private String description;
        private LocalDate date;
        private BigDecimal priceUsd;
        private Partner author;
        private Partner translator;
        private long sales;
        private long rating;
        private long rates;
        private Color avgColor;
        private double complexity;
        private String tags;
        private Set<Localization> localizations;

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
     * @return the uuid
     */
    public UUID getUuid() {
        return uuid;
    }

    /**
     * @param uuid the uuid to set
     */
    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }
}
