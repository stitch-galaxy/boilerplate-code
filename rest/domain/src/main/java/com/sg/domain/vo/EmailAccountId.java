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
public class EmailAccountId extends UuidId {

    public static EmailAccountId create()
    {
        return new EmailAccountId(UUID.randomUUID());
    }
    
    public EmailAccountId(UUID id) {
        super(id);
    }
}
