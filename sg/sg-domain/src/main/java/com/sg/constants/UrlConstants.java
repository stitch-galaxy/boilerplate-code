/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.constants;

import static com.sg.constants.UrlConstants.SG_ROOT_USER_NAME;
import org.joda.time.LocalDate;

/**
 *
 * @author tarasev
 */
public class UrlConstants {

    public static final String SG_DOMAIN_NAME = "stitchgalaxy.com";
    public static final String SG_ROOT_USER_NAME = "root";
    public static final String SG_ROOT_USER_EMAIL = SG_ROOT_USER_NAME + "@" + SG_DOMAIN_NAME;
    public static final String SG_ROOT_USER_FIRST_PWD = SG_ROOT_USER_NAME;
    public static final LocalDate SG_ROOT_USER_BIRTH_DATE = new LocalDate(2013, 01, 01);
}
