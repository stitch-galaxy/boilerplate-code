/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.sg_rest_api.integration.test.configuration;

import com.jayway.restassured.RestAssured;
import static com.jayway.restassured.RestAssured.get;
import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.config.SSLConfig.sslConfig;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;
import com.sg.rest.http.CustomHeaders;
import com.sg.rest.operationstatus.InstallStatus;
import com.sg.constants.RequestPath;
import com.sg.rest.operationstatus.SigninStatus;
import com.sg.dto.request.SigninDto;
import com.sg.sg_rest_api.integration.test.SigninFlowTest;
import java.nio.charset.StandardCharsets;
import org.apache.http.HttpStatus;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.equalTo;
import org.springframework.beans.factory.annotation.Value;

/**
 *
 * @author tarasev
 */
public class IntegrationTestInitializer {

    @Value("${admin.email}")
    private String ADMIN_EMAIL;
    @Value("${admin.password}")
    private String ADMIN_PASSWORD;
    @Value("${user.email}")
    private String USER_EMAIL;
    @Value("${user.password}")
    private String USER_PASSWORD;
    @Value("${rest_api.base_uri}")
    private String BASE_URI;

    private static volatile boolean baseUriInitialized = false;

    public void setBaseUri() {
        if (!baseUriInitialized) {
            synchronized (SigninFlowTest.class) {
                if (!baseUriInitialized) {
                    RestAssured.baseURI = BASE_URI;
                    RestAssured.useRelaxedHTTPSValidation();
                    RestAssured.config = RestAssured.config().sslConfig(sslConfig().allowAllHostnames());
                    baseUriInitialized = true;
                }
            }
        }
    }

    private static volatile boolean sgInstalled = false;

    public void installSg() {
        if (!sgInstalled) {
            synchronized (SigninFlowTest.class) {
                if (!sgInstalled) {
                    try {
                        get(RequestPath.REQUEST_INSTALL)
                                .then().log().all()
                                .statusCode(HttpStatus.SC_OK)
                                .body("status", anyOf(equalTo(InstallStatus.STATUS_SUCCESS), equalTo(InstallStatus.STATUS_ALREADY_COMPLETED)))
                                .contentType(ContentType.JSON);
                    } catch (Throwable e) {
                        int i = 0;
                    }

                    sgInstalled = true;
                }
            }
        }
    }

    private static volatile boolean adminAuthTokenRequested = false;
    private String adminAuthToken;

    public void requestAdminAuthToken() {
        if (!adminAuthTokenRequested) {
            synchronized (SigninFlowTest.class) {
                if (!adminAuthTokenRequested) {

                    SigninDto dto = new SigninDto();
                    dto.setEmail(ADMIN_EMAIL);
                    dto.setPassword(ADMIN_PASSWORD);
                    Response r = given().log().all()
                            .contentType(ContentType.JSON.withCharset(StandardCharsets.UTF_8))
                            .body(dto)
                            .when()
                            .post(RequestPath.REQUEST_SIGNIN);

                    r.then().log().all()
                            .statusCode(HttpStatus.SC_OK)
                            .contentType(ContentType.JSON)
                            .body("status", equalTo(SigninStatus.STATUS_SUCCESS));
                    this.adminAuthToken = r.getHeader(CustomHeaders.X_AUTH_TOKEN);

                    adminAuthTokenRequested = true;
                }
            }
        }
    }

    private static volatile boolean userAuthTokenRequested = false;
    private String userAuthToken;

    public void requestUserAuthToken() {
        if (!userAuthTokenRequested) {
            synchronized (SigninFlowTest.class) {
                if (!userAuthTokenRequested) {

                    SigninDto dto = new SigninDto();
                    dto.setEmail(ADMIN_EMAIL);
                    dto.setPassword(ADMIN_PASSWORD);
                    Response r = given().log().all()
                            .contentType(ContentType.JSON.withCharset(StandardCharsets.UTF_8))
                            .body(dto)
                            .when()
                            .post(RequestPath.REQUEST_SIGNIN);

                    r.then().log().all()
                            .statusCode(HttpStatus.SC_OK)
                            .contentType(ContentType.JSON)
                            .body("status", equalTo(SigninStatus.STATUS_SUCCESS));
                    this.adminAuthToken = r.getHeader(CustomHeaders.X_AUTH_TOKEN);

                    userAuthTokenRequested = true;
                }
            }
        }
    }

    public String getAdminAuthToken() {
        return adminAuthToken;
    }

    public String getUserAuthToken() {
        return userAuthToken;
    }
}
