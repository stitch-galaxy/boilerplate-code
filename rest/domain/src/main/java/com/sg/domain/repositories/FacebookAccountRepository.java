/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.domain.repositories;

import com.sg.domain.ar.FacebookAccount;
import com.sg.domain.vo.FacebookAccountId;

/**
 *
 * @author Admin
 */
public interface FacebookAccountRepository {

    public FacebookAccount getAccountById(FacebookAccountId id);
}
