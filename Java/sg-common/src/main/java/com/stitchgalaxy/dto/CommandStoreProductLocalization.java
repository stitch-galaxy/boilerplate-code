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
public class CommandStoreProductLocalization {
    
    private Long productId;
    private ProductLocalizationInfo productLocalization;

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
     * @return the productLocalization
     */
    public ProductLocalizationInfo getProductLocalization() {
        return productLocalization;
    }

    /**
     * @param productLocalization the productLocalization to set
     */
    public void setProductLocalization(ProductLocalizationInfo productLocalization) {
        this.productLocalization = productLocalization;
    }
}
