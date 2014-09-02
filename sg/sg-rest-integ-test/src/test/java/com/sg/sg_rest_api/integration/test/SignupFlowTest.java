/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.sg_rest_api.integration.test;

import static com.jayway.restassured.RestAssured.given;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;
import com.sg.constants.CompleteSignupStatus;
import com.sg.constants.CustomHttpHeaders;
import com.sg.constants.RequestPath;
import com.sg.constants.Roles;
import com.sg.constants.SigninStatus;
import com.sg.constants.SignupStatus;
import com.sg.constants.TokenExpirationType;
import com.sg.domain.service.AuthToken;
import com.sg.domain.service.SgCryptoService;
import com.sg.domain.service.exception.SgCryptoException;
import com.sg.dto.request.CompleteSignupDto;
import com.sg.dto.request.SigninDto;
import com.sg.dto.request.SignupDto;
import com.sg.dto.response.AccountPrincipalDto;
import com.sg.sg_rest_api.integration.test.configuration.IntegrationTestContext;
import com.sg.sg_rest_api.integration.test.configuration.IntegrationTestInitializer;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import org.apache.http.HttpStatus;
import org.codehaus.jackson.map.ObjectMapper;
import static org.hamcrest.Matchers.*;
import org.joda.time.Instant;
import org.joda.time.LocalDate;
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

    @Autowired
    private SgCryptoService security;

    @Before
    public void setup() {
        integrationTestInitializer.setBaseUri();
        integrationTestInitializer.installSg();
        integrationTestInitializer.requestAdminAuthToken();
        integrationTestInitializer.requestUserAuthToken();
    }

    private static final LocalDate USER_BIRTH_DATE = LocalDate.parse("1984-07-10");
    private static final String USER_LAST_NAME = "Тарасов";
    private static final String USER_FIRST_NAME = "Евгений";
    private static final String USER_DOMAIN = "@вышивка.рф";
    private static final String USER_PASSWORD = "1ХорошийПароль";
    private static final String BAD_PASSWORD = "плохой";

    @Test
    public void testSignupFlow() throws IOException, SgCryptoException {

        String USER_EMAIL = USER_FIRST_NAME + Instant.now().getMillis() + USER_DOMAIN;

        SignupDto dto = new SignupDto();
        dto.setEmail(USER_EMAIL);
        dto.setUserFirstName(USER_FIRST_NAME);
        dto.setUserLastName(USER_LAST_NAME);

        //SignupStatus.STATUS_SUCCESS
        Response r = given().log().all()
                .contentType(ContentType.JSON.withCharset(StandardCharsets.UTF_8))
                .body(jacksonObjectMapper.writeValueAsString(dto))
                .header(CustomHttpHeaders.X_AUTH_TOKEN, integrationTestInitializer.getAdminAuthToken())
                .when()
                .post(RequestPath.REQUEST_SIGNUP_ADMIN_USER);

        r.then().log().all()
                .statusCode(HttpStatus.SC_OK)
                .contentType(ContentType.JSON)
                .body("status", equalTo(SignupStatus.STATUS_SUCCESS))
                .header(CustomHttpHeaders.X_ACCOUNT_ID, any(String.class));

        String sAccountId = r.getHeader(CustomHttpHeaders.X_ACCOUNT_ID);
        Long accountId = Long.parseLong(sAccountId);

        //SignupStatus.STATUS_CONFIRMATION_EMAIL_RESENT
        given().log().all()
                .contentType(ContentType.JSON.withCharset(StandardCharsets.UTF_8))
                .body(jacksonObjectMapper.writeValueAsString(dto))
                .header(CustomHttpHeaders.X_AUTH_TOKEN, integrationTestInitializer.getAdminAuthToken())
                .when()
                .post(RequestPath.REQUEST_SIGNUP_ADMIN_USER)
                .then().log().all()
                .statusCode(HttpStatus.SC_OK)
                .contentType(ContentType.JSON)
                .body("status", equalTo(SignupStatus.STATUS_CONFIRMATION_EMAIL_RESENT));

        //SigninStatus.STATUS_EMAIL_NOT_VERIFIED
        SigninDto signInDto = new SigninDto();
        signInDto.setEmail(USER_EMAIL);
        signInDto.setPassword(USER_PASSWORD);
        r = given().log().all()
                .contentType(ContentType.JSON.withCharset(StandardCharsets.UTF_8))
                .body(jacksonObjectMapper.writeValueAsString(signInDto))
                .when()
                .post(RequestPath.REQUEST_SIGNIN);

        r.then().log().all()
                .statusCode(HttpStatus.SC_OK)
                .contentType(ContentType.JSON)
                .body("status", equalTo(SigninStatus.STATUS_EMAIL_NOT_VERIFIED));

        //Complete signup
        //Generate auth tokenAccountPrincipalDtoccountDtoAccountPrincipalDtoo = new AccountDto();
        AccountPrincipalDto accountDto = new AccountPrincipalDto();
        accountDto.setId(accountId);
        accountDto.setRoles(Arrays.asList(new String[]{Roles.ROLE_ADMIN, Roles.ROLE_USER}));
        AuthToken token = new AuthToken(accountDto, TokenExpirationType.LONG_TOKEN, Instant.now());
        String authToken = security.encryptSecurityToken(token);

        //VERIFICATION: BAD PASSWORD
        CompleteSignupDto completeSignupDto = new CompleteSignupDto();
        completeSignupDto.setPassword(BAD_PASSWORD);
        r = given().log().all()
                .contentType(ContentType.JSON.withCharset(StandardCharsets.UTF_8))
                .body(jacksonObjectMapper.writeValueAsString(completeSignupDto))
                .header(CustomHttpHeaders.X_AUTH_TOKEN, authToken)
                .when()
                .post(RequestPath.REQUEST_COMPLETE_SIGNUP);

        r.then().log().all()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .contentType(ContentType.JSON)
                .body("fieldErrors", hasSize(1))
                .body("fieldErrors", containsInAnyOrder(
                                CompleteSignupDto.FIELD_COMPLETE_SIGNUP_PASSWORD
                        ));

        //CompleteSignupStatus.STATUS_SUCCESS
        completeSignupDto = new CompleteSignupDto();
        completeSignupDto.setPassword(USER_PASSWORD);

        r = given().log().all()
                .contentType(ContentType.JSON.withCharset(StandardCharsets.UTF_8))
                .body(jacksonObjectMapper.writeValueAsString(completeSignupDto))
                .header(CustomHttpHeaders.X_AUTH_TOKEN, authToken)
                .when()
                .post(RequestPath.REQUEST_COMPLETE_SIGNUP);

        r.then().log().all()
                .statusCode(HttpStatus.SC_OK)
                .contentType(ContentType.JSON)
                .body("status", equalTo(CompleteSignupStatus.STATUS_SUCCESS));

        //CompleteSignupStatus.STATUS_ALREADY_COMPLETED
        r = given().log().all()
                .contentType(ContentType.JSON.withCharset(StandardCharsets.UTF_8))
                .body(jacksonObjectMapper.writeValueAsString(completeSignupDto))
                .header(CustomHttpHeaders.X_AUTH_TOKEN, authToken)
                .when()
                .post(RequestPath.REQUEST_COMPLETE_SIGNUP);

        r.then().log().all()
                .statusCode(HttpStatus.SC_OK)
                .contentType(ContentType.JSON)
                .body("status", equalTo(CompleteSignupStatus.STATUS_ALREADY_COMPLETED));

        //CompleteSignupStatus.STATUS_ACCOUNT_NOT_FOUND
        //Generate wrong auth tokeAccountPrincipalDtoaccountDto = new AccountDto();
        accountDto.setId(Long.MAX_VALUE);
        accountDto.setRoles(Arrays.asList(new String[]{Roles.ROLE_ADMIN, Roles.ROLE_USER}));
        token = new AuthToken(accountDto, TokenExpirationType.LONG_TOKEN, Instant.now());
        authToken = security.encryptSecurityToken(token);

        r = given().log().all()
                .contentType(ContentType.JSON.withCharset(StandardCharsets.UTF_8))
                .body(jacksonObjectMapper.writeValueAsString(completeSignupDto))
                .header(CustomHttpHeaders.X_AUTH_TOKEN, authToken)
                .when()
                .post(RequestPath.REQUEST_COMPLETE_SIGNUP);

        r.then().log().all()
                .statusCode(HttpStatus.SC_OK)
                .contentType(ContentType.JSON)
                .body("status", equalTo(CompleteSignupStatus.STATUS_ACCOUNT_NOT_FOUND));

        //Signin successfully
        signInDto = new SigninDto();
        signInDto.setEmail(USER_EMAIL);
        signInDto.setPassword(USER_PASSWORD);
        r = given().log().all()
                .contentType(ContentType.JSON.withCharset(StandardCharsets.UTF_8))
                .body(jacksonObjectMapper.writeValueAsString(signInDto))
                .when()
                .post(RequestPath.REQUEST_SIGNIN);

        r.then().log().all()
                .statusCode(HttpStatus.SC_OK)
                .contentType(ContentType.JSON)
                .body("status", equalTo(SigninStatus.STATUS_SUCCESS))
                .header(CustomHttpHeaders.X_AUTH_TOKEN, any(String.class));
    }

    @Test
    public void testSignupValidations() throws IOException {
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
                                SignupDto.FIELD_SIGNUP_USER_FIRST_NAME,
                                SignupDto.FIELD_SIGNUP_USER_LAST_NAME
                        ));
    }

}
