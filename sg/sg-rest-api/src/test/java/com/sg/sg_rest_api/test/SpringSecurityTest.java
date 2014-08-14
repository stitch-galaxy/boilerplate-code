/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.sg_rest_api.test;

import com.sg.sg_rest_api.utils.CustomMediaTypes;
import com.sg.sg_rest_api.test.configuration.WebApplicationIntegrationTestContext;
import com.sg.domain.service.SgService;
import com.sg.dto.ThreadDto;
import com.sg.constants.CustomHttpHeaders;
import com.sg.constants.Roles;
import com.sg.sg_rest_api.configuration.ServletContext;
import com.sg.constants.RequestPath;
import com.sg.dto.UserDto;
import com.sg.sg_rest_api.security.AuthToken;
import com.sg.sg_rest_api.security.Security;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import org.codehaus.jackson.map.ObjectMapper;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.hamcrest.Matchers.not;
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

    private static final String AIDA_14 = "Aida 14";

    private MockMvc mockMvc;

    @Autowired
    private SgService serviceMock;

    @Autowired
    Security security;

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
        ThreadDto dto = new ThreadDto();
        dto.setCode(AIDA_14);

        when(serviceMock.listThreads()).thenReturn(Arrays.asList(dto));

        mockMvc.perform(get(RequestPath.REQUEST_THREAD_LIST))
                .andExpect(status().isOk())
                .andExpect(content().contentType(CustomMediaTypes.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].code", is(AIDA_14)));
        verify(serviceMock, times(1)).listThreads();
        verifyNoMoreInteractions(serviceMock);
    }

    @Test
    public void testSecureResourceWithoutAuthToken() throws IOException, Exception {
        ThreadDto threadDto = new ThreadDto();
        threadDto.setCode(AIDA_14);

        ObjectMapper mapper = new ObjectMapper();

        mockMvc.perform(
                post(RequestPath.REQUEST_THREAD_ADD)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(threadDto)))
                .andExpect(status().is(HttpServletResponse.SC_UNAUTHORIZED))
                .andExpect(content().contentType(CustomMediaTypes.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.error", not(isEmptyOrNullString())))
                .andExpect(jsonPath("$.refNumber", not(isEmptyOrNullString())));
        verifyNoMoreInteractions(serviceMock);
    }

    @Test
    public void testSecureResourceWithAuthToken() throws IOException, Exception {

        UserDto dto = new UserDto();
        dto.setEmail("test@example.com");
        dto.setPassword("password");
        List<String> roles = new ArrayList<String>();
        roles.add(Roles.ROLE_ADMIN);
        dto.setRoles(roles);
        
        AuthToken token = new AuthToken(dto);
        String authToken = security.getTokenString(token);

        ThreadDto threadDto = new ThreadDto();
        threadDto.setCode(AIDA_14);

        ObjectMapper mapper = new ObjectMapper();

        mockMvc.perform(
                post(RequestPath.REQUEST_THREAD_ADD)
                .header(CustomHttpHeaders.X_AUTH_TOKEN, authToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(threadDto)))
                .andExpect(status().isOk())
                .andExpect(content().bytes(new byte[0]));
        verify(serviceMock, times(1)).create(threadDto);
        verifyNoMoreInteractions(serviceMock);
    }

    @Test
    public void testSecureResourceWithBadAuthToken() throws IOException, Exception {
        ThreadDto threadDto = new ThreadDto();
        threadDto.setCode(AIDA_14);

        ObjectMapper mapper = new ObjectMapper();

        mockMvc.perform(
                post(RequestPath.REQUEST_THREAD_ADD)
                .header(CustomHttpHeaders.X_AUTH_TOKEN, BAD_TOKEN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(threadDto)))
                .andExpect(status().is(HttpServletResponse.SC_UNAUTHORIZED))
                .andExpect(content().contentType(CustomMediaTypes.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.error", not(isEmptyOrNullString())))
                .andExpect(jsonPath("$.refNumber", not(isEmptyOrNullString())));
        verifyNoMoreInteractions(serviceMock);
    }
    public static final String BAD_TOKEN = "BAD_TOKEN";
}
