/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sg.constants;

import org.joda.time.Duration;
import org.joda.time.Period;

/**
 *
 * @author tarasev
 */
public class TokenExpirationIntervals {
    public static final Duration USER_SESSION_TOKEN_EXPIRATION_INTERVAL = Period.days(1).toStandardDuration();
    public static final Duration LONG_TOKEN_EXPIRATION_INTERVAL = Period.days(30).toStandardDuration();
}
