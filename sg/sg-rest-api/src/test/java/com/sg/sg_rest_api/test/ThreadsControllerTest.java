/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.sg_rest_api.test;

import com.sg.domain.dto.ThreadDto;
import com.sg.domain.service.SgService;
import com.sg.sg_rest_api.controllers.RequestPath;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.hamcrest.Matchers.*;
import org.junit.Before;
import org.mockito.Mockito;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 *
 * @author tarasev
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {WebApplicationTestContext.class})
@WebAppConfiguration
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
        first.setCode("Aida 14");

        ThreadDto second = new ThreadDto();
        first.setCode("Aida 18");
        when(serviceMock.listThreads()).thenReturn(Arrays.asList(first, second));

        mockMvc.perform(get(RequestPath.REQUEST_THREAD_LIST))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(2)));
                //.andExpect(jsonPath("$[0].code", is("Aida 14")))
                //.andExpect(jsonPath("$[1].code", is("Aida 18")));
        verify(serviceMock, times(1)).listThreads();
        verifyNoMoreInteractions(serviceMock);
    }
}
