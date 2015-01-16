/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.domain.ar;

import com.sg.domain.enumerations.Role;
import com.sg.domain.enumerations.Sex;
import java.util.List;
import org.joda.time.LocalDate;

/**
 *
 * @author tarasev
 */
public interface AccountDbState {
    public Long getAccountId();
    public String getEmail();
    public String getPasswordHash();
    public Boolean getIsEmailVerified();
    public String getUserFirstName();
    public String getUserLastName();
    public LocalDate getUserBirthDate();
    public Sex getSex();
    public List<Role> getRoles();
    public List<Long> getPdfPurchases();
}
