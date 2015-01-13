/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.dto.request.cqrs;

/**
 *
 * @author tarasev
 */
public class GetAccountRolesRequest {

    private Long accountId;
    
    public GetAccountRolesRequest()
    {}

    /**
     * @return the accountId
     */
    public Long getAccountId() {
        return accountId;
    }

    /**
     * @param accountId the accountId to set
     */
    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }
}
