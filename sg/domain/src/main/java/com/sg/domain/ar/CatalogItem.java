/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.domain.ar;

import com.sg.domail.vo.CatalogItemId;
import com.sg.domail.vo.SchemaId;
import com.sg.domain.enumerations.CatalogItemState;
import java.math.BigDecimal;
import org.joda.time.LocalDate;

/**
 *
 * @author tarasev
 */
public class CatalogItem {

    private CatalogItemId id;
    private BigDecimal pdfPrice;
    private final SchemaId schemaId;
    private LocalDate date;
    private CatalogItemState state;

    public CatalogItem(SchemaId schemaId, BigDecimal pdfPrice) {
        if (schemaId == null || pdfPrice == null || pdfPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalStateException();
        }
        this.schemaId = schemaId;
        this.pdfPrice = pdfPrice;
        this.state = CatalogItemState.DRAFT;
    }

    public void publish() {
        if (!state.equals(CatalogItemState.DRAFT)) {
            throw new IllegalArgumentException();
        }
        this.state = CatalogItemState.PUBLISHED;
        this.date = LocalDate.now();
    }
    
    public void block()
    {
        if (!state.equals(CatalogItemState.PUBLISHED))
            throw new IllegalArgumentException();
        this.state = CatalogItemState.BLOCKED;
    }
    
    public void unblock()
    {
        if (!state.equals(CatalogItemState.BLOCKED))
            throw new IllegalArgumentException();
        this.state = CatalogItemState.PUBLISHED;
    }

    /**
     * @return the id
     */
    public CatalogItemId getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(CatalogItemId id) {
        this.id = id;
    }

    /**
     * @return the pdfPrice
     */
    public BigDecimal getPdfPrice() {
        return pdfPrice;
    }

    /**
     * @param pdfPrice the pdfPrice to set
     */
    public void setPdfPrice(BigDecimal pdfPrice) {
        if (pdfPrice == null || pdfPrice.compareTo(BigDecimal.ZERO) < 0)
            throw new IllegalArgumentException();
        this.pdfPrice = pdfPrice;
    }

    /**
     * @return the schemaId
     */
    public SchemaId getSchemaId() {
        return schemaId;
    }

    /**
     * @return the date
     */
    public LocalDate getDate() {
        return date;
    }
}
