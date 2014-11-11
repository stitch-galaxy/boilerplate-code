/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.sg_rest_api.test;

import com.sg.sg_rest_api.utils.CustomMediaTypes;
import com.sg.sg_rest_api.test.configuration.WebApplicationIntegrationTestContext;
import com.sg.domain.service.SgService;
import com.sg.rest.http.CustomHeaders;
import com.sg.rest.errorcodes.ErrorCodes;
import com.sg.sg_rest_api.configuration.ServletContext;
import com.sg.rest.apipath.RequestPath;
import com.sg.domain.constants.Roles;
import com.sg.domain.service.exception.SgAccountNotFoundException;
import com.sg.dto.response.AccountRolesDto;
import com.sg.rest.webtoken.TokenExpirationStandardDurations;
import com.sg.rest.webtoken.WebTokenService;
import com.sun.javafx.scene.control.skin.VirtualFlow;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
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
@ContextConfiguration(classes = {WebApplicationIntegrationTestContext.class, ServletContext.class})
public class SpringSecurityTest {

    private static final long ACCOUNT_ID = 1L;
    private static final String BAD_TOKEN = "BAD_TOKEN";
    private static final String PATH_DO_NOT_EXIST = "/PATH_DO_NOT_EXIST";
    private static final AccountRolesDto accountRolesDto;

    static {
        accountRolesDto = new AccountRolesDto();
        accountRolesDto.setRoles(Arrays.asList(new String[]{Roles.ROLE_USER, Roles.ROLE_ADMIN}));
    }

    private MockMvc mockMvc;

    @Autowired
    private SgService serviceMock;

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
        Mockito.reset(serviceMock);

        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .addFilter(springSecurityFilterChain)
                .build();
    }

    @Test
    public void testUnsecureResource() throws Exception {
        mockMvc.perform(get(RequestPath.REQUEST_PING))
                .andExpect(status().isOk())
                .andExpect(content().bytes(new byte[0]));
        verify(serviceMock, times(1)).ping();
        verifyNoMoreInteractions(serviceMock);
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
        mockMvc.perform(get(RequestPath.REQUEST_PING + PATH_DO_NOT_EXIST))
                .andExpect(status().is(HttpServletResponse.SC_NOT_FOUND))
                .andExpect(content().bytes(new byte[0]));
    }
    
    @Test 
    public void testNonExistentResourceWhichStartsFromSecurePath() throws Exception
    {
        mockMvc.perform(get(RequestPath.REQUEST_SECURE_PING + PATH_DO_NOT_EXIST))
                .andExpect(status().is(HttpServletResponse.SC_UNAUTHORIZED))
                .andExpect(content().contentType(CustomMediaTypes.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.error", not(isEmptyOrNullString())))
                .andExpect(jsonPath("$.refNumber", not(isEmptyOrNullString())))
                .andExpect(jsonPath("$.errorCode", is(ErrorCodes.TOKEN_AUTHENTICATION_NO_TOKEN)));
    }
    

    @Test
    public void testSecureResourceWithoutAuthToken() throws IOException, Exception {
        mockMvc.perform(get(RequestPath.REQUEST_SECURE_PING))
                .andExpect(status().is(HttpServletResponse.SC_UNAUTHORIZED))
                .andExpect(content().contentType(CustomMediaTypes.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.error", not(isEmptyOrNullString())))
                .andExpect(jsonPath("$.refNumber", not(isEmptyOrNullString())))
                .andExpect(jsonPath("$.errorCode", is(ErrorCodes.TOKEN_AUTHENTICATION_NO_TOKEN)));
        verifyNoMoreInteractions(serviceMock);
    }

    @Test
    public void testSecureResourceWithAuthToken() throws IOException, Exception {
        String authToken = webSecurityService.generateToken(ACCOUNT_ID, Instant.now(), TokenExpirationStandardDurations.WEB_SESSION_TOKEN_EXPIRATION_DURATION);
        when(serviceMock.getAccountRoles(ACCOUNT_ID)).thenReturn(accountRolesDto);

        mockMvc.perform(get(RequestPath.REQUEST_SECURE_PING).header(CustomHeaders.X_AUTH_TOKEN, authToken))
                .andExpect(status().isOk())
                .andExpect(content().bytes(new byte[0]));

        verify(serviceMock, times(1)).ping();
        verify(serviceMock, times(1)).getAccountRoles(ACCOUNT_ID);
        verifyNoMoreInteractions(serviceMock);
    }
    
    @Test
    public void testSecureResourceWithAuthTokenButNonSufficientRights() throws IOException, Exception {
        String authToken = webSecurityService.generateToken(ACCOUNT_ID, Instant.now(), TokenExpirationStandardDurations.WEB_SESSION_TOKEN_EXPIRATION_DURATION);
        AccountRolesDto accountRolesDto = new AccountRolesDto();
        accountRolesDto.setRoles(new ArrayList<String>());
        when(serviceMock.getAccountRoles(ACCOUNT_ID)).thenReturn(accountRolesDto);

        mockMvc.perform(get(RequestPath.REQUEST_SECURE_PING).header(CustomHeaders.X_AUTH_TOKEN, authToken))
                .andExpect(status().is(HttpServletResponse.SC_FORBIDDEN))
                .andExpect(content().contentType(CustomMediaTypes.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.error", not(isEmptyOrNullString())))
                .andExpect(jsonPath("$.refNumber", not(isEmptyOrNullString())))
                .andExpect(jsonPath("$.errorCode", is(ErrorCodes.ACCESS_DENIED)));

        verify(serviceMock, times(1)).getAccountRoles(ACCOUNT_ID);
        verifyNoMoreInteractions(serviceMock);
    }

    @Test
    public void testSecureResourceWithAuthTokenButForNonExistentAccount() throws IOException, Exception {
        String authToken = webSecurityService.generateToken(ACCOUNT_ID, Instant.now(), TokenExpirationStandardDurations.WEB_SESSION_TOKEN_EXPIRATION_DURATION);
        doThrow(new SgAccountNotFoundException(ACCOUNT_ID)).when(serviceMock).getAccountRoles(ACCOUNT_ID);

        mockMvc.perform(get(RequestPath.REQUEST_SECURE_PING).header(CustomHeaders.X_AUTH_TOKEN, authToken))
                .andExpect(status().is(HttpServletResponse.SC_UNAUTHORIZED))
                .andExpect(content().contentType(CustomMediaTypes.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.error", not(isEmptyOrNullString())))
                .andExpect(jsonPath("$.refNumber", not(isEmptyOrNullString())))
                .andExpect(jsonPath("$.errorCode", is(ErrorCodes.TOKEN_AUTHENTICATION_ACCOUNT_DO_NOT_EXISTS)));

        verify(serviceMock, times(1)).getAccountRoles(ACCOUNT_ID);
        verifyNoMoreInteractions(serviceMock);
    }

    @Test
    public void testExpiredAuthToken() throws IOException, Exception {
        String authToken = webSecurityService.generateToken(ACCOUNT_ID, Instant.now().minus(TokenExpirationStandardDurations.WEB_SESSION_TOKEN_EXPIRATION_DURATION).minus(Duration.standardHours(1)), TokenExpirationStandardDurations.WEB_SESSION_TOKEN_EXPIRATION_DURATION);

        mockMvc.perform(get(RequestPath.REQUEST_SECURE_PING).header(CustomHeaders.X_AUTH_TOKEN, authToken))
                .andExpect(status().is(HttpServletResponse.SC_UNAUTHORIZED))
                .andExpect(content().contentType(CustomMediaTypes.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.error", not(isEmptyOrNullString())))
                .andExpect(jsonPath("$.refNumber", not(isEmptyOrNullString())))
                .andExpect(jsonPath("$.errorCode", is(ErrorCodes.TOKEN_AUTHENTICATION_TOKEN_EXPIRED)));
        verifyNoMoreInteractions(serviceMock);
    }

    @Test
    public void testSecureResourceWithBadAuthToken() throws IOException, Exception {
        mockMvc.perform(get(RequestPath.REQUEST_SECURE_PING).header(CustomHeaders.X_AUTH_TOKEN, BAD_TOKEN))
                .andExpect(status().is(HttpServletResponse.SC_UNAUTHORIZED))
                .andExpect(content().contentType(CustomMediaTypes.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.error", not(isEmptyOrNullString())))
                .andExpect(jsonPath("$.refNumber", not(isEmptyOrNullString())))
                .andExpect(jsonPath("$.errorCode", is(ErrorCodes.TOKEN_AUTHENTICATION_BAD_TOKEN)));
        verifyNoMoreInteractions(serviceMock);
    }

}
