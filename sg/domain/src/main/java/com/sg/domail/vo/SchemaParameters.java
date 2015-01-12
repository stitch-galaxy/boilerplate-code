/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.domail.vo;

import java.util.Set;

/**
 *
 * @author tarasev
 */
public class SchemaParameters {
    private final int width;
    private final int height;
    private final int colors;
    private final Fabric fabric;
    private final Set<Producer> threadProducers;
    
    public SchemaParameters(int width,
            int height,
            int colors,
            Fabric fabric,
            Set<Producer> threadProducers)
    {
        if (width <= 0
                || height <=0
                || colors <=0
                || fabric == null
                || threadProducers == null
                || threadProducers.size() <= 0
                )
            throw new IllegalStateException();
        this.width = width;
        this.height = height;
        this.colors = colors;
        this.fabric = fabric;
        this.threadProducers = threadProducers;
    }

    /**
     * @return the width
     */
    public int getWidth() {
        return width;
    }

    /**
     * @return the height
     */
    public int getHeight() {
        return height;
    }

    /**
     * @return the colors
     */
    public int getColors() {
        return colors;
    }

    /**
     * @return the fabric
     */
    public Fabric getFabric() {
        return fabric;
    }

    /**
     * @return the threadProducers
     */
    public Set<Producer> getThreadProducers() {
        return threadProducers;
    }
}
