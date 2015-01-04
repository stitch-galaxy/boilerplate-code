/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.domain.entities.jpa;

import com.sg.domain.enumerations.Sex;
import java.util.List;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Transient;
import org.hibernate.annotations.Type;
import org.joda.time.LocalDate;

/**
 *
 * @author tarasev
 */
@Entity(name = "account")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @Column(name = "id")
    private Long id;

    @Column(name="user_first_name")
    private String userFirstName;
    
    @Column(name="user_last_name")
    private String userLastName;
    
    @Column(name="nickname")
    private String nickname;
    
    @Column(name="user_birth_date")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDate")
    private LocalDate userBirthDate;
    
    @Column(name = "sex")
    private Integer sexPersistentId;
    
    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "password")
    private String password;
    
    @Column(name = "is_email_verified", nullable = false)
    private Boolean emailVerified;

    @ElementCollection
    @CollectionTable(
            name = "account_role",
            joinColumns = @JoinColumn(name = "account_id")
    )
    @Column(name = "role_name")
    private List<String> roles;

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
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
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the roles
     */
    public List<String> getRoles() {
        return roles;
    }

    /**
     * @param roles the roles to set
     */
    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    /**
     * @return the emailVerified
     */
    public Boolean getEmailVerified() {
        return emailVerified;
    }

    /**
     * @param emailVerified the emailVerified to set
     */
    public void setEmailVerified(Boolean emailVerified) {
        this.emailVerified = emailVerified;
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
     * @return the sex
     */
    public Sex getSex() {
        return Sex.getSexFromPersistenId(sexPersistentId);
    }

    /**
     * @param sex the sex to set
     */
    public void setSex(Sex sex) {
        this.sexPersistentId = sex.getPersistentId();
    }

    /**
     * @return the nickname
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * @param nickname the nickname to set
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
