/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sg.rest.authtoken.enumerations;

import org.joda.time.Duration;

/**
 *
 * @author tarasev
 */
public enum TokenExpirationStandardDurations {
    
    WEB_SESSION_TOKEN_EXPIRATION_DURATION(Duration.standardDays(1)),
    EMAIL_TOKEN_EXPIRATION_DURATION(Duration.standardDays(30));
    
    private final Duration duration;
    
    TokenExpirationStandardDurations(Duration duration)
    {
        this.duration = duration;
    }

    /**
     * @return the duration
     */
    public Duration getDuration() {
        return duration;
    }
}
