/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sg.rest.apipath;

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
    
    public static final String REQUEST_PING = REST_USER_API_PATH + "/ping";
    public static final String REQUEST_SECURE_PING = REST_SECURE_USER_PATH + "/ping";
    
    public static final String REQUEST_THREAD_ADD = REST_SECURE_ADMIN_PATH + "/thread/create";
    public static final String REQUEST_THREAD_DELETE = REST_SECURE_ADMIN_PATH + "/thread/delete";
    public static final String REQUEST_THREAD_LIST = REST_USER_API_PATH + "/threads";
    public static final String REQUEST_THREAD_UPDATE = REST_SECURE_ADMIN_PATH + "/thread/update";
    
    public static final String REQUEST_CANVAS_ADD = REST_SECURE_ADMIN_PATH + "/canvas/create";
    public static final String REQUEST_CANVAS_DELETE = REST_SECURE_ADMIN_PATH + "/canvas/delete";
    public static final String REQUEST_CANVAS_LIST = REST_USER_API_PATH + "/canvases";
    public static final String REQUEST_CANVAS_UPDATE = REST_SECURE_ADMIN_PATH + "/canvas/update";
    
    public static final String REQUEST_SIGNIN = REST_USER_API_PATH + "/signin";
    public static final String REQUEST_SIGNUP_USER = REST_USER_API_PATH + "user/signup";
    public static final String REQUEST_SIGNUP_ADMIN_USER = REST_SECURE_ADMIN_PATH + "admin/signup";
    public static final String REQUEST_COMPLETE_SIGNUP = REST_USER_API_PATH + "user/signup/complete";
    //TODO: think of security level for this request
    //public static final String REQUEST_RESET_PASSWORD = REST_USER_API_PATH + "user/password/reset";
    
}
