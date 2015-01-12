/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.domail.vo;

import com.sg.domain.enumerations.Sex;
import org.joda.time.LocalDate;

/**
 *
 * @author tarasev
 */
public class UserInfo {
    private final String firstName;
    private final String lastName;
    private final LocalDate userBirthDate;
    private final Sex sex;
    public UserInfo(String firstName, String lastName, LocalDate userBirthDate, Sex sex)
    {
        this.firstName = firstName;
        this.lastName = lastName;
        this.userBirthDate = userBirthDate;
        this.sex = sex;
    }

    /**
     * @return the firstName
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * @return the lastName
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * @return the userBirthDate
     */
    public LocalDate getUserBirthDate() {
        return userBirthDate;
    }

    /**
     * @return the sex
     */
    public Sex getSex() {
        return sex;
    }
}
