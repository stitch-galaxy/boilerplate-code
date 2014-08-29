/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.sg_rest_api.test;

import com.sg.sg_rest_api.utils.CustomMediaTypes;
import com.sg.sg_rest_api.test.configuration.WebApplicationIntegrationTestContext;
import com.sg.domain.service.SgService;
import com.sg.dto.request.ThreadCreateDto;
import com.sg.constants.CustomHttpHeaders;
import com.sg.constants.OperationStatus;
import com.sg.constants.Roles;
import com.sg.sg_rest_api.configuration.ServletContext;
import com.sg.constants.RequestPath;
import com.sg.constants.TokenExpirationIntervals;
import com.sg.constants.TokenExpirationType;
import com.sg.domain.service.AuthToken;
import com.sg.domain.service.SgCryptoService;
import com.sg.dto.response.AccountPrincipalDto;
import java.io.IOException;
import java.util.Arrays;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import org.codehaus.jackson.map.ObjectMapper;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.hamcrest.Matchers.not;
import org.joda.time.Instant;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
    private static final AccountPrincipalDto accountDto;
    
    static
    {
        accountDto = new AccountPrincipalDto();
        accountDto.setEmailVerified(Boolean.FALSE);
        accountDto.setId(ACCOUNT_ID);
        accountDto.setRoles(Arrays.asList(new String[]{Roles.ROLE_USER, Roles.ROLE_ADMIN}));
    }

    private MockMvc mockMvc;

    @Autowired
    private SgService serviceMock;

    @Autowired
    SgCryptoService security;

    @Resource
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
    public void testSecureResourceWithoutAuthToken() throws IOException, Exception {
        mockMvc.perform(get(RequestPath.REQUEST_SECURE_PING))
                .andExpect(status().is(HttpServletResponse.SC_UNAUTHORIZED))
                .andExpect(content().contentType(CustomMediaTypes.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.error", not(isEmptyOrNullString())))
                .andExpect(jsonPath("$.refNumber", not(isEmptyOrNullString())));
        verifyNoMoreInteractions(serviceMock);
    }
    
    @Test
    public void testSecureResourceWithAuthToken() throws IOException, Exception {
        AuthToken token = new AuthToken(accountDto, TokenExpirationType.USER_SESSION_TOKEN, Instant.now());
        String authToken = security.encryptSecurityToken(token);

        Long l = ACCOUNT_ID;
        mockMvc.perform(get(RequestPath.REQUEST_SECURE_PING).header(CustomHttpHeaders.X_AUTH_TOKEN, authToken))
                .andExpect(status().isOk())
                .andExpect(content().string(l.toString()));
        
        verify(serviceMock, times(1)).ping();
        verifyNoMoreInteractions(serviceMock);
    }

    @Test
    public void testExpiredAuthToken() throws IOException, Exception {
        AuthToken token = new AuthToken(accountDto, TokenExpirationType.USER_SESSION_TOKEN, Instant.now().minus(TokenExpirationIntervals.USER_SESSION_TOKEN_EXPIRATION_INTERVAL));
        token.setExpirationInstantMillis(Instant.now().getMillis() - 1);
        String authToken = security.encryptSecurityToken(token);

        mockMvc.perform(get(RequestPath.REQUEST_SECURE_PING).header(CustomHttpHeaders.X_AUTH_TOKEN, authToken))
                .andExpect(status().is(HttpServletResponse.SC_UNAUTHORIZED))
                .andExpect(content().contentType(CustomMediaTypes.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.error", not(isEmptyOrNullString())))
                .andExpect(jsonPath("$.refNumber", not(isEmptyOrNullString())));
        verifyNoMoreInteractions(serviceMock);
    }

    @Test
    public void testSecureResourceWithBadAuthToken() throws IOException, Exception {
        mockMvc.perform(get(RequestPath.REQUEST_SECURE_PING).header(CustomHttpHeaders.X_AUTH_TOKEN, BAD_TOKEN))
                .andExpect(status().is(HttpServletResponse.SC_UNAUTHORIZED))
                .andExpect(content().contentType(CustomMediaTypes.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.error", not(isEmptyOrNullString())))
                .andExpect(jsonPath("$.refNumber", not(isEmptyOrNullString())));
        verifyNoMoreInteractions(serviceMock);
    }
    
}
