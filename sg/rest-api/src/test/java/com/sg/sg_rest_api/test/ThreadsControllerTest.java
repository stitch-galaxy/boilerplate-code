/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.sg_rest_api.test;

import com.sg.sg_rest_api.utils.CustomMediaTypes;
import com.sg.sg_rest_api.test.configuration.WebApplicationUnitTestContext;
import com.sg.dto.request.ThreadCreateDto;
import com.sg.domain.service.SgService;
import com.sg.sg_rest_api.configuration.ServletContext;
import com.sg.rest.apipath.RequestPath;
import com.sg.rest.operationstatus.SigninStatus;
import com.sg.rest.operationstatus.ThreadOperationStatus;
import com.sg.domain.service.exception.SgEmailNonVerifiedException;
import com.sg.domain.service.exception.SgThreadAlreadyExistsException;
import com.sg.domain.service.exception.SgThreadNotFoundException;
import com.sg.dto.request.ThreadDeleteDto;
import com.sg.dto.request.ThreadUpdateDto;
import com.sg.dto.response.ThreadsListDto;
import java.io.IOException;
import java.util.ArrayList;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import org.codehaus.jackson.map.ObjectMapper;

import static org.hamcrest.Matchers.*;
import org.junit.Before;
import org.mockito.Mockito;
import static org.mockito.Mockito.*;
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
@ContextConfiguration(classes = {WebApplicationUnitTestContext.class, ServletContext.class})
public class ThreadsControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private SgService serviceMock;

    @Autowired
    private WebApplicationContext webApplicationContext;
    
    @Autowired
    private ObjectMapper jacksonObjectMapper;

    @Before
    public void setUp() {
        //We have to reset our mock between tests because the mock objects
        //are managed by the Spring container. If we would not reset them,
        //stubbing and verified behavior would "leak" from one test to another.
        Mockito.reset(serviceMock);

        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .build();
    }

    private static final String DMC = "DMC";
    private static final String ANCHOR = "Anchor";

    private static final ThreadCreateDto dmcThreadDto;
    private static final ThreadCreateDto anchorThreadDto;
    
    private static final ThreadsListDto.ThreadInfo dmcThreadInfoDto;
    private static final ThreadsListDto.ThreadInfo anchorThreadInfoDto;
    
    private static final ThreadDeleteDto dmcThreadDeleteDto;
    
    private static final ThreadUpdateDto dmcThreadUpdateDto;

    static {
        dmcThreadDto = new ThreadCreateDto();
        dmcThreadDto.setCode(DMC);

        anchorThreadDto = new ThreadCreateDto();
        anchorThreadDto.setCode(ANCHOR);
        
        dmcThreadInfoDto = new ThreadsListDto.ThreadInfo();
        dmcThreadInfoDto.setCode(DMC);
        
        anchorThreadInfoDto = new ThreadsListDto.ThreadInfo();
        anchorThreadInfoDto.setCode(ANCHOR);
        
        dmcThreadDeleteDto = new ThreadDeleteDto();
        dmcThreadDeleteDto.setCode(DMC);
        
        dmcThreadUpdateDto = new ThreadUpdateDto();
        dmcThreadUpdateDto.setRefCode(DMC);
        dmcThreadUpdateDto.setCode(ANCHOR);
    }

    @Test
    public void testList() throws Exception {
        
        ThreadsListDto threadsList = new ThreadsListDto();
        List<ThreadsListDto.ThreadInfo> list = new ArrayList<ThreadsListDto.ThreadInfo>();
        list.add(dmcThreadInfoDto);
        list.add(anchorThreadInfoDto);
        threadsList.setThreads(list);
        
        when(serviceMock.listThreads()).thenReturn(threadsList);

        mockMvc.perform(get(RequestPath.REQUEST_THREAD_LIST))
                .andExpect(status().isOk())
                .andExpect(content().contentType(CustomMediaTypes.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.threads", hasSize(2)))
                .andExpect(jsonPath("$.threads.[0].code", is(DMC)))
                .andExpect(jsonPath("$.threads[1].code", is(ANCHOR)));
        verify(serviceMock, times(1)).listThreads();
        verifyNoMoreInteractions(serviceMock);
    }

    @Test
    public void testCreateAlreadyExists() throws IOException, Exception {
        doThrow(new SgThreadAlreadyExistsException(dmcThreadDto.getCode())).when(serviceMock).create(dmcThreadDto);

        mockMvc.perform(
                post(RequestPath.REQUEST_THREAD_ADD)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jacksonObjectMapper.writeValueAsString(dmcThreadDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is(ThreadOperationStatus.THREAD_ALREADY_EXISTS)));
        verify(serviceMock, times(1)).create(dmcThreadDto);
        verifyNoMoreInteractions(serviceMock);
    }

    @Test
    public void testCreate() throws IOException, Exception {
        mockMvc.perform(
                post(RequestPath.REQUEST_THREAD_ADD)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jacksonObjectMapper.writeValueAsString(dmcThreadDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is(ThreadOperationStatus.STATUS_SUCCESS)));
        verify(serviceMock, times(1)).create(dmcThreadDto);
        verifyNoMoreInteractions(serviceMock);
    }
    
    @Test
    public void testDeleteNotFound() throws IOException, Exception {
        doThrow(new SgThreadNotFoundException(dmcThreadDeleteDto.getCode())).when(serviceMock).delete(dmcThreadDeleteDto);

        mockMvc.perform(
                post(RequestPath.REQUEST_THREAD_DELETE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jacksonObjectMapper.writeValueAsString(dmcThreadDeleteDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is(ThreadOperationStatus.THREAD_NOT_FOUND)));
        verify(serviceMock, times(1)).delete(dmcThreadDeleteDto);
        verifyNoMoreInteractions(serviceMock);
    }

    @Test
    public void testDelete() throws IOException, Exception {
        mockMvc.perform(
                post(RequestPath.REQUEST_THREAD_DELETE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jacksonObjectMapper.writeValueAsString(dmcThreadDeleteDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is(ThreadOperationStatus.STATUS_SUCCESS)));
        verify(serviceMock, times(1)).delete(dmcThreadDeleteDto);
        verifyNoMoreInteractions(serviceMock);
    }
    
    @Test
    public void testUpdateNotFound() throws IOException, Exception {
        doThrow(new SgThreadNotFoundException(dmcThreadUpdateDto.getRefCode())).when(serviceMock).update(dmcThreadUpdateDto);

        mockMvc.perform(
                post(RequestPath.REQUEST_THREAD_UPDATE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jacksonObjectMapper.writeValueAsString(dmcThreadUpdateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is(ThreadOperationStatus.THREAD_NOT_FOUND)));
        verify(serviceMock, times(1)).update(dmcThreadUpdateDto);
        verifyNoMoreInteractions(serviceMock);
    }
    
    @Test
    public void testUpdateAlreadyExists() throws IOException, Exception {
        doThrow(new SgThreadAlreadyExistsException(dmcThreadUpdateDto.getRefCode())).when(serviceMock).update(dmcThreadUpdateDto);

        mockMvc.perform(
                post(RequestPath.REQUEST_THREAD_UPDATE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jacksonObjectMapper.writeValueAsString(dmcThreadUpdateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is(ThreadOperationStatus.THREAD_ALREADY_EXISTS)));
        verify(serviceMock, times(1)).update(dmcThreadUpdateDto);
        verifyNoMoreInteractions(serviceMock);
    }
}
