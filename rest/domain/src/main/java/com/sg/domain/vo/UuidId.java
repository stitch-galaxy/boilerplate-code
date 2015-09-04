/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.domain.vo;

import java.util.UUID;

/**
 *
 * @author Admin
 */
public class UuidId {

    private final UUID id;

    public UuidId(UUID id) {
        this.id = id;
    }

    /**
     * @return the id
     */
    public UUID getId() {
        return id;
    }
}
