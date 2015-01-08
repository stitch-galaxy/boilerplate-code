/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.domain.ar;

import com.sg.domail.vo.AccountId;
import com.sg.domail.vo.FacebookAccount;
import com.sg.domail.vo.Permissions;
import com.sg.domail.vo.SiteAccount;
import com.sg.domail.vo.UserInfo;

/**
 *
 * @author tarasev
 */
public class Account {

    private AccountId id;
    private SiteAccount siteAccount;
    private FacebookAccount facebookAccount;
    private UserInfo userInfo;
    private Permissions permissions;

    public Account(String email, String password) {
        SiteAccount siteAccount = new SiteAccount(email, password, Boolean.FALSE);
        this.siteAccount = siteAccount;
    }

    public Account(FacebookAccount facebookAccount) {
        if (facebookAccount == null) {
            throw new IllegalArgumentException();
        }
        this.facebookAccount = facebookAccount;
    }

    /**
     * @return the siteAccount
     */
    public SiteAccount getSiteAccount() {
        return siteAccount;
    }

    /**
     * @param siteAccount the siteAccount to set
     */
    public void setSiteAccount(SiteAccount siteAccount) {
        this.siteAccount = siteAccount;
    }

    /**
     * @return the facebookAccount
     */
    public FacebookAccount getFacebookAccount() {
        return facebookAccount;
    }

    /**
     * @param facebookAccount the facebookAccount to set
     */
    public void setFacebookAccount(FacebookAccount facebookAccount) {
        this.facebookAccount = facebookAccount;
    }

    /**
     * @return the userInfo
     */
    public UserInfo getUserInfo() {
        return userInfo;
    }

    /**
     * @param userInfo the userInfo to set
     */
    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    /**
     * @return the permissions
     */
    public Permissions getPermissions() {
        return permissions;
    }

    /**
     * @param permissions the permissions to set
     */
    public void setPermissions(Permissions permissions) {
        this.permissions = permissions;
    }

    /**
     * @return the id
     */
    public AccountId getAccountId() {
        return id;
    }

    /**
     * @param accountId the id to set
     */
    public void setAccountId(AccountId accountId) {
        this.id = accountId;
    }
}
