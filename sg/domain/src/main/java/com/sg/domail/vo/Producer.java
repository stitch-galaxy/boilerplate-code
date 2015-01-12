/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.domail.vo;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 *
 * @author tarasev
 */
public class Producer {

    private final String producer;

    public Producer(String producer) {
        if (producer == null || producer.isEmpty()) {
            throw new IllegalArgumentException();
        }
        this.producer = producer;
    }

    /**
     * @return the producer
     */
    public String getProducer() {
        return producer;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        }

        Producer other = (Producer) obj;
        return new EqualsBuilder().
                append(this.producer, other.producer).
                isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(producer).build();
    }
}
