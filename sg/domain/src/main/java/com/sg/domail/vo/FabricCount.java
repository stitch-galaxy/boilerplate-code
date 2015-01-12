/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.domail.vo;

import java.math.BigDecimal;

/**
 *
 * @author tarasev
 */
public class FabricCount {

    private final BigDecimal count;

    public FabricCount(BigDecimal count) {
        if (count == null || count.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException();
        }
        this.count = count;
    }

    /**
     * @return the count
     */
    public BigDecimal getCount() {
        return count;
    }
}
