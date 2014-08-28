/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.sg_rest_api.integration.test;

import static com.jayway.restassured.RestAssured.given;
import com.jayway.restassured.http.ContentType;
import com.sg.constants.CustomHttpHeaders;
import com.sg.constants.RequestPath;
import com.sg.dto.request.SignupDto;
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
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 *
 * @author tarasev
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {IntegrationTestContext.class})
public class SignupFlowTest {

    @Autowired
    private IntegrationTestInitializer integrationTestInitializer;
    
    @Autowired
    private ObjectMapper jacksonObjectMapper;

    @Before
    public void setup() {
        integrationTestInitializer.setBaseUri();
        integrationTestInitializer.installSg();
        integrationTestInitializer.requestAdminAuthToken();
        integrationTestInitializer.requestUserAuthToken();
    }
    
    @Test
    public void testSignupValidations() throws IOException
    {
        SignupDto dto = new SignupDto();
        dto.setEmail("тарас");
        dto.setUserFirstName("");
        dto.setUserLastName("");
        
        given().log().all()
                .contentType(ContentType.JSON.withCharset(StandardCharsets.UTF_8))
                .body(jacksonObjectMapper.writeValueAsString(dto))
                .header(CustomHttpHeaders.X_AUTH_TOKEN, integrationTestInitializer.getAdminAuthToken())
                .when()
                .post(RequestPath.REQUEST_SIGNUP_ADMIN_USER)
                .then().log().all()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .contentType(ContentType.JSON)
                .body("fieldErrors", hasSize(4))
                .body("fieldErrors", containsInAnyOrder(
                        SignupDto.FIELD_SIGNUP_EMAIL, 
                        SignupDto.FIELD_SIGNUP_USER_BIRTH_DATE, 
                        SignupDto.FIELD_SIGNUP_USER_FIRST_NAME, 
                        SignupDto.FIELD_SIGNUP_USER_LAST_NAME
                ));
    }
    
}
