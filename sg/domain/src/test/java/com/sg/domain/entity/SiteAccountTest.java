/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.domain.entity;

import com.sg.domail.entity.SiteAccount;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author tarasev
 */
public class SiteAccountTest {
    @Test
    public void testPassword() {
        SiteAccount account = new SiteAccount(TEST_EMAIL, FIRST_PASSWORD);
        account.verifyEmail();
        account.changePassword(SECOND_PASSWORD);
        Assert.assertEquals(true, account.isPasswordCorrect(SECOND_PASSWORD));
    }
    
    
    @Test(expected = IllegalStateException.class)
    public void testIllegalStateException() {
        SiteAccount account = new SiteAccount(TEST_EMAIL, FIRST_PASSWORD);
        account.changePassword(SECOND_PASSWORD);
    }
    
    private static final String FIRST_PASSWORD = "password";
    private static final String SECOND_PASSWORD = "anotherPassword";
    private static final String TEST_EMAIL = "test@gmail.com";
}
