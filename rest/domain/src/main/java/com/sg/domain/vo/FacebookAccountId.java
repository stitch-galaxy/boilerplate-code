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
public class FacebookAccountId extends UuidId {

    public static FacebookAccountId create() {
        return new FacebookAccountId(UUID.randomUUID());
    }

    public FacebookAccountId(UUID id) {
        super(id);
    }
}
