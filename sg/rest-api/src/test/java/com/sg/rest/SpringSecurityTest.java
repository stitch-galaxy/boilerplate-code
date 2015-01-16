/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.rest;

import com.sg.domain.enumerations.Role;
import com.sg.rest.enumerations.CustomMediaTypes;
import com.sg.rest.spring.test.WebApplicationIntegrationTestContext;
import com.sg.rest.enumerations.HttpCustomHeaders;
import com.sg.rest.spring.SpringServletContextConfiguration;
import com.sg.rest.path.RequestPath;
import com.sg.domain.operation.OperationExecutor;
import com.sg.dto.operation.status.GetAccountRolesStatus;
import com.sg.dto.operation.GetAccountRolesOperation;
import com.sg.dto.operation.response.GetAccountRolesResponse;
import com.sg.rest.authtoken.enumerations.TokenExpirationStandardDurations;
import com.sg.rest.enumerations.AuthentificationFailureStatus;
import com.sg.rest.webtoken.WebTokenService;
import java.io.IOException;
import java.util.EnumSet;
import java.util.Set;
import javax.servlet.http.HttpServletResponse;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.hamcrest.Matchers.not;
import org.joda.time.Duration;
import org.joda.time.Instant;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 *
 * @author tarasev
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {WebApplicationIntegrationTestContext.class, SpringServletContextConfiguration.class})
public class SpringSecurityTest {

    private static final long ACCOUNT_ID = 1L;
    private static final String BAD_TOKEN = "BAD_TOKEN";
    private static final String PATH_DO_NOT_EXIST = "/PATH_DO_NOT_EXIST";

    private MockMvc mockMvc;
    
    @Autowired
    private OperationExecutor<GetAccountRolesResponse, GetAccountRolesOperation> handler;

    @Autowired
    private WebTokenService webSecurityService;

    @Autowired
    private FilterChainProxy springSecurityFilterChain;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before
    public void setUp() {
        //We have to reset our mock between tests because the mock objects
        //are managed by the Spring container. If we would not reset them,
        //stubbing and verified behavior would "leak" from one test to another.
        Mockito.reset(handler);

        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .addFilter(springSecurityFilterChain)
                .build();
    }

    @Test
    public void testUnsecureResource() throws Exception {
        mockMvc.perform(get(RequestPath.TEST_REQUEST))
                .andExpect(status().isOk())
                .andExpect(content().bytes(new byte[0]));
    }
    
    @Test
    public void testNonExistentResource() throws Exception {
        mockMvc.perform(get(PATH_DO_NOT_EXIST))
                .andExpect(status().is(HttpServletResponse.SC_NOT_FOUND))
                .andExpect(content().bytes(new byte[0]));
    }
    
    @Test 
    public void testNonExistentResourceWhichStartsFromPublicPath() throws Exception
    {
        mockMvc.perform(get(RequestPath.TEST_REQUEST + PATH_DO_NOT_EXIST))
                .andExpect(status().is(HttpServletResponse.SC_NOT_FOUND))
                .andExpect(content().bytes(new byte[0]));
    }
    
    @Test 
    public void testNonExistentResourceWhichStartsFromSecurePath() throws Exception
    {
        mockMvc.perform(get(RequestPath.TEST_SECURE_REQUEST + PATH_DO_NOT_EXIST))
                .andExpect(status().is(HttpServletResponse.SC_UNAUTHORIZED))
                .andExpect(content().contentType(CustomMediaTypes.APPLICATION_JSON_UTF8.getMediatype()))
                .andExpect(jsonPath("$.eventRef.id", not(isEmptyOrNullString())))
                .andExpect(jsonPath("$.status", is(AuthentificationFailureStatus.TOKEN_AUTHENTICATION_NO_TOKEN.name())));
    }
    

    @Test
    public void testSecureResourceWithoutAuthToken() throws IOException, Exception {
        mockMvc.perform(get(RequestPath.TEST_SECURE_REQUEST))
                .andExpect(status().is(HttpServletResponse.SC_UNAUTHORIZED))
                .andExpect(content().contentType(CustomMediaTypes.APPLICATION_JSON_UTF8.getMediatype()))
                .andExpect(jsonPath("$.eventRef.id", not(isEmptyOrNullString())))
                .andExpect(jsonPath("$.status", is(AuthentificationFailureStatus.TOKEN_AUTHENTICATION_NO_TOKEN.name())));
    }

    @Test
    public void testSecureResourceWithAuthToken() throws IOException, Exception {
        String authToken = webSecurityService.generateToken(ACCOUNT_ID, Instant.now(), TokenExpirationStandardDurations.WEB_SESSION_TOKEN_EXPIRATION_DURATION);
        Set<Role> roles = EnumSet.noneOf(Role.class);
        roles.add(Role.USER);
        roles.add(Role.ADMIN);
        when(handler.handle(new GetAccountRolesOperation(ACCOUNT_ID))).thenReturn(new GetAccountRolesResponse(roles));

        mockMvc.perform(get(RequestPath.TEST_SECURE_REQUEST).header(HttpCustomHeaders.AUTH_TOKEN_HEADER.getHeader(), authToken))
                .andExpect(status().isOk())
                .andExpect(content().bytes(new byte[0]));
    }
    
    @Test
    public void testSecureResourceWithAuthTokenButNonSufficientRights() throws IOException, Exception {
        String authToken = webSecurityService.generateToken(ACCOUNT_ID, Instant.now(), TokenExpirationStandardDurations.WEB_SESSION_TOKEN_EXPIRATION_DURATION);
        Set<Role> roles = EnumSet.noneOf(Role.class);
        when(handler.handle(new GetAccountRolesOperation(ACCOUNT_ID))).thenReturn(new GetAccountRolesResponse(roles));

        mockMvc.perform(get(RequestPath.TEST_SECURE_REQUEST).header(HttpCustomHeaders.AUTH_TOKEN_HEADER.getHeader(), authToken))
                .andExpect(status().is(HttpServletResponse.SC_FORBIDDEN))
                .andExpect(content().contentType(CustomMediaTypes.APPLICATION_JSON_UTF8.getMediatype()))
                .andExpect(jsonPath("$.eventRef.id", not(isEmptyOrNullString())));
    }

    @Test
    public void testSecureResourceWithAuthTokenButForNonExistentAccount() throws IOException, Exception {
        String authToken = webSecurityService.generateToken(ACCOUNT_ID, Instant.now(), TokenExpirationStandardDurations.WEB_SESSION_TOKEN_EXPIRATION_DURATION);
        GetAccountRolesOperation request = new GetAccountRolesOperation(ACCOUNT_ID);
        
        when(handler.handle(new GetAccountRolesOperation(ACCOUNT_ID))).thenReturn(new GetAccountRolesResponse(GetAccountRolesStatus.STATUS_ACCOUNT_NOT_FOUND));

        mockMvc.perform(get(RequestPath.TEST_SECURE_REQUEST).header(HttpCustomHeaders.AUTH_TOKEN_HEADER.getHeader(), authToken))
                .andExpect(status().is(HttpServletResponse.SC_UNAUTHORIZED))
                .andExpect(content().contentType(CustomMediaTypes.APPLICATION_JSON_UTF8.getMediatype()))
                .andExpect(jsonPath("$.eventRef.id", not(isEmptyOrNullString())))
                .andExpect(jsonPath("$.status", is(AuthentificationFailureStatus.TOKEN_AUTHENTICATION_ACCOUNT_DO_NOT_EXISTS.name())));
    }

    @Test
    public void testExpiredAuthToken() throws IOException, Exception {
        String authToken = webSecurityService.generateToken(ACCOUNT_ID, Instant.now().minus(TokenExpirationStandardDurations.WEB_SESSION_TOKEN_EXPIRATION_DURATION.getDuration()).minus(Duration.standardHours(1)), TokenExpirationStandardDurations.WEB_SESSION_TOKEN_EXPIRATION_DURATION);

        mockMvc.perform(get(RequestPath.TEST_SECURE_REQUEST).header(HttpCustomHeaders.AUTH_TOKEN_HEADER.getHeader(), authToken))
                .andExpect(status().is(HttpServletResponse.SC_UNAUTHORIZED))
                .andExpect(content().contentType(CustomMediaTypes.APPLICATION_JSON_UTF8.getMediatype()))
                .andExpect(jsonPath("$.eventRef.id", not(isEmptyOrNullString())))
                .andExpect(jsonPath("$.status", is(AuthentificationFailureStatus.TOKEN_AUTHENTICATION_TOKEN_EXPIRED.name())));
    }

    @Test
    public void testSecureResourceWithBadAuthToken() throws IOException, Exception {
        mockMvc.perform(get(RequestPath.TEST_SECURE_REQUEST).header(HttpCustomHeaders.AUTH_TOKEN_HEADER.getHeader(), BAD_TOKEN))
                .andExpect(status().is(HttpServletResponse.SC_UNAUTHORIZED))
                .andExpect(content().contentType(CustomMediaTypes.APPLICATION_JSON_UTF8.getMediatype()))
                .andExpect(jsonPath("$.eventRef.id", not(isEmptyOrNullString())))
                .andExpect(jsonPath("$.status", is(AuthentificationFailureStatus.TOKEN_AUTHENTICATION_BAD_TOKEN.name())));
    }

}
