/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sg.constants;

/**
 *
 * @author tarasev
 */
public class PasswordRegexp {
    //http://stackoverflow.com/questions/3802192/regexp-java-for-password-validation
    //public static final String PASSWORD_REGULAR_EXPRESSION = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[~!@#$%^&*_+-=]).{6,20})";
    
    // length of password from minimum 7 letters to maximum 20 letters, contains at lease 1 digit
    public static final String PASSWORD_REGULAR_EXPRESSION = "((?=.*\\d).{7,20})";
}
