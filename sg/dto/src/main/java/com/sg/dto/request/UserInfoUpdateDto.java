/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sg.dto.request;

import com.sg.domain.enumerations.Sex;
import com.sg.dto.serialization.JodaLocalDateJsonDeserializer;
import com.sg.dto.serialization.JodaLocalDateJsonSerializer;
import javax.validation.constraints.Past;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.text.WordUtils;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.joda.time.LocalDate;

/**
 *
 * @author tarasev
 */
public class UserInfoUpdateDto {
 
    @Past
    @JsonSerialize(using = JodaLocalDateJsonSerializer.class)
    @JsonDeserialize(using = JodaLocalDateJsonDeserializer.class)
    private LocalDate userBirthDate;

    private String userFirstName;
    
    private String userLastName;
 
    private Sex sex;
    
    private String nickname;
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        }

        UserInfoUpdateDto other = (UserInfoUpdateDto) obj;
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
     * @return the sex
     */
    public Sex getSex() {
        return sex;
    }

    /**
     * @param sex the sex to set
     */
    public void setSex(Sex sex) {
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
        this.nickname = null;
        if (nickname != null) {
            this.nickname = WordUtils.capitalizeFully(nickname.trim());
        }
    }
}
