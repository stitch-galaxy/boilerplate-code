/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg;

import com.jayway.restassured.RestAssured;
import com.sg.constants.RequestPath;
import org.junit.Test;
import static com.jayway.restassured.RestAssured.*;
import static com.jayway.restassured.config.DecoderConfig.decoderConfig;
import static com.jayway.restassured.config.EncoderConfig.encoderConfig;
import static com.jayway.restassured.config.RestAssuredConfig.newConfig;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;
import com.sg.constants.CustomHttpHeaders;
import com.sg.constants.InstallStatus;
import com.sg.constants.SigninStatus;
import com.sg.constants.ThreadOperationStatus;
import com.sg.constants.UrlConstants;
import com.sg.dto.request.SigninDto;
import com.sg.dto.request.ThreadCreateDto;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.apache.http.HttpStatus;
import org.codehaus.jackson.map.ObjectMapper;
import static org.hamcrest.Matchers.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;

/**
 *
 * @author tarasev
 */
@RunWith(OrderedRunner.class)
public class FlowTest {

    private static final String BASE_URI = "http://localhost:8080/sg-rest-api";
    
    private String rootAuthToken;
    private String adminAuthToken;
    private String userAuthToken;

    public FlowTest() {
    }
    
 
    @BeforeClass
    public static void setup() {
        RestAssured.baseURI = BASE_URI;
        RestAssured.config = newConfig().decoderConfig(decoderConfig().defaultContentCharset("UTF-8"));
        RestAssured.config = newConfig().encoderConfig(encoderConfig().defaultContentCharset("UTF-8"));
    }
    
    @Before
    public void getAuthToken(){
        SigninDto dto = new SigninDto();
        dto.setEmail(UrlConstants.SG_ROOT_USER_EMAIL);
        dto.setPassword(UrlConstants.SG_ROOT_USER_FIRST_PWD);
        Response r = given().log().all()
                .contentType(ContentType.JSON.withCharset(StandardCharsets.UTF_8))
                .body(dto)
                .when()
                .post(RequestPath.REQUEST_SIGNIN);
        
        r.then().log().all()
                .statusCode(HttpStatus.SC_OK)
                .contentType(ContentType.JSON)
                .body("status", equalTo(SigninStatus.STATUS_SUCCESS));
        this.rootAuthToken = r.getHeader(CustomHttpHeaders.X_AUTH_TOKEN);
    }

    @Test
    @Order(order = 1)
    public void testinstallSg() {

        get(RequestPath.REQUEST_INSTALL)
                .then().log().all()
                .statusCode(HttpStatus.SC_OK)
                .body("status", equalTo(InstallStatus.STATUS_SUCCESS))
                .contentType(ContentType.JSON);
        
        get(RequestPath.REQUEST_INSTALL)
                .then().log().all()
                .statusCode(HttpStatus.SC_OK)
                .body("status", equalTo(InstallStatus.STATUS_ALREADY_COMPLETED))
                .contentType(ContentType.JSON);
    }
    
    @Test
    @Order(order = 2)
    public void signinAsRoot() {
//        dto.setPassword(INCORRECT_ROOT_PASSWORD);
//        given().log().all()
//                .contentType(ContentType.JSON.withCharset(StandardCharsets.UTF_8))
//                .body(dto)
//                .when()
//                .post(RequestPath.REQUEST_SIGNIN)
//                .then().log().all()
//                .statusCode(HttpStatus.SC_OK)
//                .contentType(ContentType.JSON)
//                .body("status", equalTo(SigninStatus.STATUS_WRONG_PASSWORD));
//        
//        given().log().all()
//                .contentType(ContentType.JSON.withCharset(StandardCharsets.UTF_8))
//                .body(dto)
//                .when()
//                .post(RequestPath.REQUEST_SIGNIN)
//                .then().log().all()
//                .statusCode(HttpStatus.SC_OK)
//                .contentType(ContentType.JSON)
//                .body("status", equalTo(SigninStatus.STATUS_USER_NOT_FOUND));
    }
    
    @Test
    @Order(order = 3)
    public void addThread() throws IOException
    {
        ThreadCreateDto dto = new ThreadCreateDto();
        dto.setCode("СОБЕРИСЬ!");
        ObjectMapper mapper = new ObjectMapper();
        
        
        
        
        given().log().all()
                .contentType(ContentType.JSON)
                //.contentType(ContentType.JSON.withCharset(StandardCharsets.UTF_8))
                .header(CustomHttpHeaders.X_AUTH_TOKEN, this.rootAuthToken)
                .body(mapper.writeValueAsString(dto))
                .when()
                .post(RequestPath.REQUEST_THREAD_ADD)
                .then().log().all()
                .statusCode(HttpStatus.SC_OK)
                .contentType(ContentType.JSON)
                .body("status", equalTo(ThreadOperationStatus.STATUS_SUCCESS));
    }

    @Test
    @Order(order = 6)
    public void getThreads() {
        get(RequestPath.REQUEST_THREAD_LIST)
                .then().log().all()
                .statusCode(HttpStatus.SC_OK)
                .contentType(ContentType.JSON)
                .body("threads[1].code", equalTo("ХА-ХА"));
    }

}
