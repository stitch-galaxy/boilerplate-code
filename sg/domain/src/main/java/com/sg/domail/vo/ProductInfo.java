/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.domail.vo;

import java.math.BigDecimal;
import java.util.List;
import org.joda.time.LocalDate;

/**
 *
 * @author tarasev
 */
public class ProductInfo {

    private final Boolean blocked;
    private final Boolean published;

    private final Fabric fabric;
    private final List<Threads> usedThreads;

    private final LocalDate date;
    private final Integer width;
    private final Integer height;
    private final Integer colors;
    private final BigDecimal price;

    private final List<FileUrl> thumbnails;
    private final List<FileUrl> images;
    private final List<FileUrl> completeImages;
    private final List<FileUrl> completeImagesThumbnails;
    private final FileUrl pdf;
    private final FileUrl design;

    public ProductInfo(Boolean blocked,
            Boolean published,
            Fabric fabric,
            List<Threads> usedThreads,
            LocalDate date,
            Integer width,
            Integer height,
            Integer colors,
            BigDecimal price,
            List<FileUrl> thumbnails,
            List<FileUrl> images,
            List<FileUrl> completeImages,
            List<FileUrl> completeImagesThumbnails,
            FileUrl pdf,
            FileUrl design
    ) {
        this.blocked = blocked;
        this.published = published;
        this.fabric = fabric;
        this.usedThreads = usedThreads;
        this.date = date;
        this.width = width;
        this.height = height;
        this.colors = colors;
        this.price = price;
        this.thumbnails = thumbnails;
        this.images = images;
        this.completeImages = completeImages;
        this.completeImagesThumbnails = completeImagesThumbnails;
        this.pdf = pdf;
        this.design = design;
        
    }
}
