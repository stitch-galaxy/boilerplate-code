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
    private String firstName;
    private String lastName;
    private LocalDate userBirthDate;
    private Sex sex;
}
