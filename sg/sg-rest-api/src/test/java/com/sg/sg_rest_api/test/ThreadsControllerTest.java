/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.sg_rest_api.test;

import com.sg.dto.ThreadDto;
import com.sg.domain.service.SgService;
import com.sg.domain.service.SgServiceLayerException;
import com.sg.sg_rest_api.configuration.ServletContext;
import com.sg.sg_rest_api.controllers.RequestPath;
import java.io.IOException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import javax.servlet.http.HttpServletResponse;
import org.codehaus.jackson.map.ObjectMapper;

import static org.hamcrest.Matchers.*;
import org.junit.Before;
import org.mockito.Mockito;
import static org.mockito.Mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 *
 * @author tarasev
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {WebApplicationTestContext.class, ServletContext.class})
public class ThreadsControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private SgService serviceMock;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before
    public void setUp() {
        //We have to reset our mock between tests because the mock objects
        //are managed by the Spring container. If we would not reset them,
        //stubbing and verified behavior would "leak" from one test to another.
        Mockito.reset(serviceMock);

        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void test() throws Exception {
        ThreadDto first = new ThreadDto();
        first.setCode(AIDA_14);

        ThreadDto second = new ThreadDto();
        second.setCode(AIDA_18);
        when(serviceMock.listThreads()).thenReturn(Arrays.asList(first, second));

        mockMvc.perform(get(RequestPath.REQUEST_THREAD_LIST))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].code", is(AIDA_14)))
                .andExpect(jsonPath("$[1].code", is(AIDA_18)));
        verify(serviceMock, times(1)).listThreads();
        verifyNoMoreInteractions(serviceMock);
    }
    private static final String AIDA_18 = "Aida 18";
    private static final String AIDA_14 = "Aida 14";
    
    @Test
    public void testCreate() throws IOException, Exception {
        ThreadDto threadDto = new ThreadDto();
        threadDto.setCode(AIDA_14);
        
        ObjectMapper mapper = new ObjectMapper();
        
        mockMvc.perform(
                post(RequestPath.REQUEST_THREAD_ADD)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(threadDto)))
                .andExpect(status().isOk())
                .andExpect(content().bytes(new byte[0]));
        verify(serviceMock, times(1)).create(threadDto);
        verifyNoMoreInteractions(serviceMock);
    }
    
    @Test
    public void testCreateWithError() throws Exception
    {
        ThreadDto threadDto = new ThreadDto();
        threadDto.setCode(AIDA_14);
        
        ObjectMapper mapper = new ObjectMapper();
        
        doThrow(new SgServiceLayerException(THREAD_ALREADY_EXISTS)).when(serviceMock).create(threadDto);
        
        mockMvc.perform(
                post(RequestPath.REQUEST_THREAD_ADD)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(threadDto)))
                .andExpect(status().is(HttpServletResponse.SC_INTERNAL_SERVER_ERROR))
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.error", is(THREAD_ALREADY_EXISTS)))
                .andExpect(jsonPath("$.error", not(isEmptyOrNullString())));
        
        verify(serviceMock, times(1)).create(threadDto);
        verifyNoMoreInteractions(serviceMock);
    }
    public static final String THREAD_ALREADY_EXISTS = "Thread already exists";
}
