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
public class PdfPurchase {

    private final SchemaId schemaId;

    public PdfPurchase(SchemaId schemaId) {
        if (schemaId == null) {
            throw new IllegalArgumentException();
        }
        this.schemaId = schemaId;
    }

    /**
     * @return the schemaId
     */
    public SchemaId getSchemaId() {
        return schemaId;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        }

        PdfPurchase other = (PdfPurchase) obj;
        return new EqualsBuilder().
                append(this.schemaId, other.schemaId).
                isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(schemaId).build();
    }
}
