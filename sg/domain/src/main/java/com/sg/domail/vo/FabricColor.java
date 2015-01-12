/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.domail.vo;

/**
 *
 * @author tarasev
 */
public class FabricColor {

    private final Producer producer;
    private final Color color;

    public FabricColor(Producer producer, Color color) {
        if (producer == null || color == null) {
            throw new IllegalArgumentException();
        }
        this.producer = producer;
        this.color = color;
    }

    /**
     * @return the producer
     */
    public Producer getProducer() {
        return producer;
    }

    /**
     * @return the color
     */
    public Color getColor() {
        return color;
    }
}
