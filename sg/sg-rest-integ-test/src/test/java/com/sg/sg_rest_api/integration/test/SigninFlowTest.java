/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.sg_rest_api.integration.test;

import static com.jayway.restassured.RestAssured.given;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;
import com.sg.constants.CustomHttpHeaders;
import com.sg.constants.RequestPath;
import com.sg.constants.SigninStatus;
import com.sg.dto.request.SigninDto;
import com.sg.sg_rest_api.integration.test.configuration.IntegrationTestContext;
import com.sg.sg_rest_api.integration.test.configuration.IntegrationTestInitializer;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.apache.http.HttpStatus;
import org.codehaus.jackson.map.ObjectMapper;
import static org.hamcrest.Matchers.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 *
 * @author tarasev
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {IntegrationTestContext.class})
public class SigninFlowTest {

    @Autowired
    private IntegrationTestInitializer integrationTestInitializer;
    
    @Autowired
    private ObjectMapper jacksonObjectMapper;

    @Before
    public void setup() {
        integrationTestInitializer.setBaseUri();
        integrationTestInitializer.installSg();
    }

    @Value("${admin.email}")
    public String ADMIN_EMAIL;
    @Value("${admin.password}")
    public String ADMIN_PASSWORD;

    @Test
    public void testSuccessfullSignin() throws IOException {
        SigninDto dto = new SigninDto();
        dto.setEmail(ADMIN_EMAIL);
        dto.setPassword(ADMIN_PASSWORD);
        Response r = given().log().all()
                .contentType(ContentType.JSON.withCharset(StandardCharsets.UTF_8))
                .body(jacksonObjectMapper.writeValueAsString(dto))
                .when()
                .post(RequestPath.REQUEST_SIGNIN);

        r.then().log().all()
                .statusCode(HttpStatus.SC_OK)
                .contentType(ContentType.JSON)
                .body("status", equalTo(SigninStatus.STATUS_SUCCESS))
                .header(CustomHttpHeaders.X_AUTH_TOKEN, any(String.class));
    }

    @Test
    public void testSigninBadPassword() throws IOException {
        SigninDto dto = new SigninDto();
        dto.setEmail(ADMIN_EMAIL);
        dto.setPassword(ADMIN_PASSWORD + "плохой");
        Response r = given().log().all()
                .contentType(ContentType.JSON.withCharset(StandardCharsets.UTF_8))
                .body(jacksonObjectMapper.writeValueAsString(dto))
                .when()
                .post(RequestPath.REQUEST_SIGNIN);

        r.then().log().all()
                .statusCode(HttpStatus.SC_OK)
                .contentType(ContentType.JSON)
                .body("status", equalTo(SigninStatus.STATUS_WRONG_PASSWORD));
    }
    
    @Test
    public void testSigninUnknownUser() throws IOException {
        SigninDto dto = new SigninDto();
        dto.setEmail("плохой" + ADMIN_EMAIL);
        dto.setPassword(ADMIN_PASSWORD);
        Response r = given().log().all()
                .contentType(ContentType.JSON.withCharset(StandardCharsets.UTF_8))
                .body(jacksonObjectMapper.writeValueAsString(dto))
                .when()
                .post(RequestPath.REQUEST_SIGNIN);

        r.then().log().all()
                .statusCode(HttpStatus.SC_OK)
                .contentType(ContentType.JSON)
                .body("status", equalTo(SigninStatus.STATUS_USER_NOT_FOUND));
    }
    
    
    
    
    
    
    
    
    
    
    
//    @Test
//    public void addThread() throws IOException
//    {
//        ThreadCreateDto dto = new ThreadCreateDto();
//        dto.setCode("СОБЕРИСЬ!");
//        ObjectMapper mapper = new ObjectMapper();
//        
//        
//        
//        
//        given().log().all()
//                .contentType(ContentType.JSON)
//                //.contentType(ContentType.JSON.withCharset(StandardCharsets.UTF_8))
//                .header(CustomHttpHeaders.X_AUTH_TOKEN, this.rootAuthToken)
//                .body(mapper.writeValueAsString(dto))
//                .when()
//                .post(RequestPath.REQUEST_THREAD_ADD)
//                .then().log().all()
//                .statusCode(HttpStatus.SC_OK)
//                .contentType(ContentType.JSON)
//                .body("status", equalTo(ThreadOperationStatus.STATUS_SUCCESS));
//    }
//
//    @Test
//    public void getThreads() {
//        get(RequestPath.REQUEST_THREAD_LIST)
//                .then().log().all()
//                .statusCode(HttpStatus.SC_OK)
//                .contentType(ContentType.JSON)
//                .body("threads[1].code", equalTo("ХА-ХА"));
//    }
    
    
    
    
    
    

}
