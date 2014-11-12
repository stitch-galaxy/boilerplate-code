/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.domain.constants;

/**
 *
 * @author tarasev
 */
public enum Sex {

    MALE(1),
    FEMALE(2);

    private final Integer persistentId;

    private Sex(Integer persistentId) {
        this.persistentId = persistentId;
    }

    /**
     * @return the persistentId
     */
    public Integer getPersistentId() {
        return persistentId;
    }

    public static Sex getSexFromPersistenId(Integer persistentId) {
        if (persistentId == null) {
            return null;
        }
        switch (persistentId) {
            case 1:
                return MALE;
            case 2:
                return FEMALE;
        }
        return null;
    }

}
