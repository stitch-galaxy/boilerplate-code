/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.domain.jpa.entities;

import com.sg.domain.ar.AccountDbState;
import com.sg.domain.enumerations.Sex;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import org.joda.time.LocalDate;
import org.hibernate.annotations.Type;

/**
 *
 * @author tarasev
 */
@Entity(name = "Account")
public class AccountProjection implements AccountDbState {

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
        this.email = email;
    }

    /**
     * @return the passwordHash
     */
    public String getPasswordHash() {
        return passwordHash;
    }

    /**
     * @param passwordHash the passwordHash to set
     */
    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    /**
     * @return the isEmailVerified
     */
    public Boolean getIsEmailVerified() {
        return isEmailVerified;
    }

    /**
     * @param isEmailVerified the isEmailVerified to set
     */
    public void setIsEmailVerified(Boolean isEmailVerified) {
        this.isEmailVerified = isEmailVerified;
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
        this.userFirstName = userFirstName;
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
        this.userLastName = userLastName;
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

    /**
     * @param sex the sex to set
     */
    public void setSex(String sex) {
        this.sex = sex;
    }

    /**
     * @param roles the roles to set
     */
    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    @Override
    public Sex getSex() {
        if (sex == null || sex.isEmpty())
            return null;
        return Sex.valueOf(sex);
    }

    @Override
    public List<Long> getPdfPurchases() {
        List<Long> retVal = new ArrayList<Long>();
        if (pdfPurchases != null)
        {
            for(PdfPurchase p : pdfPurchases)
            {
                retVal.add(p.getSchemaId());
            }
        }
        return retVal;
    }

    @Override
    public List<com.sg.domain.enumerations.Role> getRoles() {
        List<com.sg.domain.enumerations.Role> retVal = new ArrayList<com.sg.domain.enumerations.Role>();
        if (roles != null)
        {
            for(Role r : roles)
            {
                retVal.add(com.sg.domain.enumerations.Role.valueOf(r.getRole()));
            }
        }
        return retVal;
    }

    @Embeddable
    public static class Role {

        @Column(name = "Role")
        private String role;

        /**
         * @return the role
         */
        public String getRole() {
            return role;
        }

        /**
         * @param role the role to set
         */
        public void setRole(String role) {
            this.role = role;
        }
    }

    @Embeddable
    public static class PdfPurchase {

        @Column(name = "SchemaId")
        private Long schemaId;

        /**
         * @return the schemaId
         */
        public Long getSchemaId() {
            return schemaId;
        }

        /**
         * @param schemaId the schemaId to set
         */
        public void setSchemaId(Long schemaId) {
            this.schemaId = schemaId;
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @Column(name = "Id")
    private Long accountId;

    @Column(name = "Email")
    private String email;

    @Column(name = "PasswordMd5Hash")
    private String passwordHash;

    @Column(name = "IsEmailVerified")
    private Boolean isEmailVerified;

    @Column(name = "UserFirstName")
    private String userFirstName;

    @Column(name = "UserLastName")
    private String userLastName;

    @Column(name = "UserBirthDate")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDate")
    private LocalDate userBirthDate;

    @Column(name = "Sex")
    private String sex;

    @ElementCollection
    @CollectionTable(
            name = "Account_Role",
            joinColumns = @JoinColumn(name = "AccountId")
    )
    private List<Role> roles;

    @ElementCollection
    @CollectionTable(
            name = "Account_PdfPurchase",
            joinColumns = @JoinColumn(name = "AccountId")
    )
    private List<PdfPurchase> pdfPurchases;
}
