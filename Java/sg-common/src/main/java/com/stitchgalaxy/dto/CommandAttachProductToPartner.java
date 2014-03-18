/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.stitchgalaxy.dto;

/**
 *
 * @author tarasev
 */
public class CommandAttachProductToPartner {
    
    private Long productId;
    private Long partnerId;

    /**
     * @return the productId
     */
    public Long getProductId() {
        return productId;
    }

    /**
     * @param productId the productId to set
     */
    public void setProductId(Long productId) {
        this.productId = productId;
    }

    /**
     * @return the partnerId
     */
    public Long getPartnerId() {
        return partnerId;
    }

    /**
     * @param partnerId the partnerId to set
     */
    public void setPartnerId(Long partnerId) {
        this.partnerId = partnerId;
    }
}
