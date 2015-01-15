/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sg.rest.path;

/**
 *
 * @author tarasev
 */
public class RequestPath {
    
    public static final String REST_PATH = "/rest";
    public static final String REST_USER_API_PATH = REST_PATH + "/api";
    public static final String REST_SECURE_PATH = REST_PATH + "/secure";
    public static final String REST_SECURE_ADMIN_PATH = REST_SECURE_PATH + "/admin";
    public static final String REST_SECURE_USER_PATH = REST_SECURE_PATH + "/user";
    
    public static final String TEST_REQUEST = REST_USER_API_PATH + "/test";
    public static final String TEST_REQUEST_THROW_EXCEPTION = REST_USER_API_PATH + "/testException";
    public static final String TEST_SECURE_REQUEST = REST_SECURE_USER_PATH + "/ping";
    
    public static final String REQUEST_SIGNIN = REST_USER_API_PATH + "/signin";
    public static final String REQUEST_SIGNUP_USER = REST_USER_API_PATH + "user/signup";
    public static final String REQUEST_SIGNUP_ADMIN_USER = REST_SECURE_ADMIN_PATH + "admin/signup";
    public static final String REQUEST_COMPLETE_SIGNUP = REST_USER_API_PATH + "user/signup/complete";
}
