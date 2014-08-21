/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.text.WordUtils;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import org.joda.time.LocalDate;

/**
 *
 * @author tarasev
 */
public class SignupDto {

    public static final String FIELD_SIGNUP_EMAIL = "SignupDto.Email";
    public static final String FIELD_SIGNUP_USER_FIRST_NAME = "SignupDto.FirstName";
    public static final String FIELD_SIGNUP_USER_LAST_NAME = "SignupDto.LastName";
    public static final String FIELD_SIGNUP_USER_BIRTH_DATE = "SignupDto.Birthdate";

    @NotBlank(message = FIELD_SIGNUP_EMAIL)
    @Email(message = FIELD_SIGNUP_EMAIL)
    private String email;
    @NotBlank(message = FIELD_SIGNUP_USER_FIRST_NAME)
    private String userFirstName;
    @NotBlank(message = FIELD_SIGNUP_USER_LAST_NAME)
    private String userLastName;
    @NotNull(message = FIELD_SIGNUP_USER_BIRTH_DATE)
    @Past
    private LocalDate userBirthDate;

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = null;
        if (email != null) {
            this.email = email.toLowerCase().trim();
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        }

        SignupDto other = (SignupDto) obj;
        return new EqualsBuilder().
                append(this.email, other.email).
                append(this.userFirstName, other.userFirstName).
                append(this.userLastName, other.userLastName).
                append(this.userBirthDate, other.userBirthDate).
                isEquals();

    }

    /**
     * @return the userFirstName
     */
    public String getUserFirstName() {
        return userFirstName;
    }

    /**
     * @param userFirstName the userFirstName to set
     */
    public void setUserFirstName(String userFirstName) {
        this.userFirstName = null;
        if (userFirstName != null) {
            this.userFirstName = WordUtils.capitalizeFully(userFirstName.trim());
        }
    }

    /**
     * @return the userLastName
     */
    public String getUserLastName() {
        return userLastName;
    }

    /**
     * @param userLastName the userLastName to set
     */
    public void setUserLastName(String userLastName) {
        this.userLastName = null;
        if (userLastName != null) {
            this.userLastName = WordUtils.capitalizeFully(userLastName.trim());
        }
    }

    /**
     * @return the userBirthDate
     */
    public LocalDate getUserBirthDate() {
        return userBirthDate;
    }

    /**
     * @param userBirthDate the userBirthDate to set
     */
    public void setUserBirthDate(LocalDate userBirthDate) {
        this.userBirthDate = userBirthDate;
    }
}
