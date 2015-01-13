/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sg.dto.response;

import com.sg.dto.serialization.JodaLocalDateJsonDeserializer;
import com.sg.dto.serialization.JodaLocalDateJsonSerializer;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.joda.time.LocalDate;

/**
 *
 * @author tarasev
 */
public class UserInfoDto {
 
    @JsonSerialize(using = JodaLocalDateJsonSerializer.class)
    @JsonDeserialize(using = JodaLocalDateJsonDeserializer.class)
    private LocalDate userBirthDate;

    private String userFirstName;
    
    private String userLastName;
 
    private String sex;
    
    private String nickname;
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        }

        UserInfoDto other = (UserInfoDto) obj;
        return new EqualsBuilder().
                append(this.getUserBirthDate(), other.getUserBirthDate()).
                append(this.getUserFirstName(), other.getUserFirstName()).
                append(this.getUserLastName(), other.getUserLastName()).
                append(this.getSex(), other.getSex()).
                append(this.getNickname(), other.getNickname()).
                isEquals();
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
     * @return the sex
     */
    public String getSex() {
        return sex;
    }

    /**
     * @param sex the sex to set
     */
    public void setSex(String sex) {
        this.sex = sex;
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
