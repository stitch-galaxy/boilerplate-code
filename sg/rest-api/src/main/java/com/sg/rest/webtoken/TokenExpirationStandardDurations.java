/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sg.rest.webtoken;

import org.joda.time.Duration;

/**
 *
 * @author tarasev
 */
public class TokenExpirationStandardDurations {
    public static final Duration WEB_SESSION_TOKEN_EXPIRATION_DURATION = Duration.standardDays(1);
    public static final Duration EMAIL_TOKEN_EXPIRATION_DURATION = Duration.standardDays(30);
}
