/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.stitchgalaxy.design_manager;

import java.awt.Color;
import java.math.BigDecimal;
import java.util.UUID;
import java.util.Date;

/**
 * class that hold design information
 * @author tarasev
 */
public class Design
{
    private UUID designUuid;
    private Date releaseDate;
    private BigDecimal priceUsd;
    private long sales;
    private long totalRating;
    private long totalRates;
    private long colors;
    private long width;
    private long height;
    private double complexity;
    private Color avgColor;
   
    private double avgRating;
    
    /**
     * Design constructor
     */
    public Design()
    {
    }

    /**
     * @return the designUuid
     */
    public UUID getDesignUuid()
    {
        return designUuid;
    }

    /**
     * @param designUuid the designUuid to set
     */
    public void setDesignUuid(UUID designUuid)
    {
        this.designUuid = designUuid;
    }

    /**
     * @return the releaseDate
     */
    public Date getReleaseDate()
    {
        return releaseDate;
    }

    /**
     * @param releaseDate the releaseDate to set
     */
    public void setReleaseDate(Date releaseDate)
    {
        this.releaseDate = releaseDate;
    }

    /**
     * @return the priceUsd
     */
    public BigDecimal getPriceUsd()
    {
        return priceUsd;
    }

    /**
     * @param priceUsd the priceUsd to set
     */
    public void setPriceUsd(BigDecimal priceUsd)
    {
        this.priceUsd = priceUsd;
    }

    /**
     * @return the sales
     */
    public long getSales()
    {
        return sales;
    }

    /**
     * @param sales the sales to set
     */
    public void setSales(long sales)
    {
        this.sales = sales;
    }

    /**
     * @return the totalRating
     */
    public long getTotalRating()
    {
        return totalRating;
    }
    
    private void calculateAvgRating()
    {
        if (totalRates != 0)
        {
            avgRating = totalRating / totalRates;
        }
        else
        {
            avgRating = 1.0;
        }
    }

    /**
     * @param totalRating the totalRating to set
     */
    public void setTotalRating(long totalRating)
    {
        this.totalRating = totalRating;
        calculateAvgRating();
    }

    /**
     * @return the totalRates
     */
    public long getTotalRates()
    {
        return totalRates;
    }

    /**
     * @param totalRates the totalRates to set
     */
    public void setTotalRates(long totalRates)
    {
        this.totalRates = totalRates;
        calculateAvgRating();
    }

    /**
     * @return the colors
     */
    public long getColors()
    {
        return colors;
    }

    /**
     * @param colors the colors to set
     */
    public void setColors(long colors)
    {
        this.colors = colors;
    }

    /**
     * @return the width
     */
    public long getWidth()
    {
        return width;
    }

    /**
     * @param width the width to set
     */
    public void setWidth(long width)
    {
        this.width = width;
    }

    /**
     * @return the height
     */
    public long getHeight()
    {
        return height;
    }

    /**
     * @param height the height to set
     */
    public void setHeight(long height)
    {
        this.height = height;
    }

    /**
     * @return the complexity
     */
    public double getComplexity()
    {
        return complexity;
    }

    /**
     * @param complexity the complexity to set
     */
    public void setComplexity(double complexity)
    {
        this.complexity = complexity;
    }

    /**
     * @return the avgColor
     */
    public Color getAvgColor()
    {
        return avgColor;
    }

    /**
     * @param avgColor the avgColor to set
     */
    public void setAvgColor(Color avgColor)
    {
        this.avgColor = avgColor;
    }
}
