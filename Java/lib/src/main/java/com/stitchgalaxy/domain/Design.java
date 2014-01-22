/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.stitchgalaxy.domain;

import java.math.BigDecimal;

/**
 *
 * @author Administrator
 */
public class Design {
    private Long id;
    private Integer width;
    private Integer height;
    private Integer colors;
    private String canvas;
    private String threads;
    private BigDecimal stitchesPerInch;
    private String thumbnailUri;
    private String fileUri;

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
}
