/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.domain.ar;

import com.sg.domail.vo.AccountId;
import com.sg.domail.vo.FacebookAccount;
import com.sg.domail.vo.PdfPurchase;
import com.sg.domail.entity.Permissions;
import com.sg.domail.entity.SiteAccount;
import com.sg.domail.vo.UserInfo;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author tarasev
 */
public class Account {

    private AccountId id;
    private SiteAccount siteAccount;
    private FacebookAccount facebookAccount;
    private UserInfo userInfo;
    private final Permissions permissions;
    private final Set<PdfPurchase> pdfPurchases;

    public Account(String email, String password, Permissions permissions) {
        this.pdfPurchases = new HashSet<PdfPurchase>();
        this.siteAccount = new SiteAccount(email, password);
        this.permissions = permissions;
    }

    public Account(FacebookAccount facebookAccount, Permissions permissions) {
        if (facebookAccount == null) {
            throw new IllegalArgumentException();
        }
        this.pdfPurchases = new HashSet<PdfPurchase>();
        this.facebookAccount = facebookAccount;
        this.permissions = permissions;
    }
    
    public void addPdfPurchase(PdfPurchase purchase)
    {
        this.pdfPurchases.add(purchase);
    }
    
    /**
     * @return the id
     */
    public AccountId getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(AccountId id) {
        this.id = id;
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
     * @return the pdfPurchases
     */
    public Set<PdfPurchase> getPdfPurchases() {
        Set<PdfPurchase> purchases = new HashSet<PdfPurchase>();
        purchases.addAll(this.pdfPurchases);
        return pdfPurchases;
    }

    /**
     * @return the siteAccount
     */
    public SiteAccount getSiteAccount() {
        return siteAccount;
    }

    /**
     * @return the facebookAccount
     */
    public FacebookAccount getFacebookAccount() {
        return facebookAccount;
    }

    public void linkFacebookAccount(FacebookAccount facebookAccount)
    {
        if (facebookAccount == null)
            throw new IllegalArgumentException();
        this.facebookAccount = facebookAccount;
    }
}
