/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.domain.ar;

import com.sg.domain.vo.Email;
import com.sg.domain.vo.EmailAccountId;
import com.sg.domain.vo.PasswordHash;

/**
 *
 * @author Admin
 */
public class EmailAccount {

    private final EmailAccountId id;
    private final Email email;
    private PasswordHash passwordHash;
    private boolean verified;

    public EmailAccount(
            EmailAccountId id,
            Email email,
            PasswordHash passwordHash,
            boolean verified
    ) {
        this.id = id;
        this.email = email;
        this.passwordHash = passwordHash;
        this.verified = verified;
    }
}
