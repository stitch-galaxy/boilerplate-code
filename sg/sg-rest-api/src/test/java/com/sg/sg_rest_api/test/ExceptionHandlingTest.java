/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.sg_rest_api.test;

import com.sg.sg_rest_api.utils.CustomMediaTypes;
import com.sg.domain.service.SgService;
import com.sg.domain.service.SgServiceLayerException;
import com.sg.dto.ThreadDto;
import com.sg.sg_rest_api.configuration.ServletContext;
import com.sg.sg_rest_api.controllers.RequestPath;
import com.sg.sg_rest_api.test.configuration.WebApplicationUnitTestContext;
import javax.servlet.http.HttpServletResponse;
import org.codehaus.jackson.map.ObjectMapper;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.hamcrest.Matchers.not;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
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
@ContextConfiguration(classes = {WebApplicationUnitTestContext.class, ServletContext.class})
public class ExceptionHandlingTest {

    private static final String AIDA_14 = "Aida 14";

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

        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .build();
    }

    @Test
    public void testExceptionDuringServiceCall() throws Exception {
        ThreadDto threadDto = new ThreadDto();
        threadDto.setCode(AIDA_14);

        ObjectMapper mapper = new ObjectMapper();

        doThrow(new SgServiceLayerException(THREAD_ALREADY_EXISTS)).when(serviceMock).create(threadDto);

        mockMvc.perform(
                post(RequestPath.REQUEST_THREAD_ADD)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(threadDto)))
                .andExpect(status().is(HttpServletResponse.SC_INTERNAL_SERVER_ERROR))
                .andExpect(content().contentType(CustomMediaTypes.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.error", is(THREAD_ALREADY_EXISTS)))
                .andExpect(jsonPath("$.refNumber", not(isEmptyOrNullString())));

        verify(serviceMock, times(1)).create(threadDto);
        verifyNoMoreInteractions(serviceMock);
    }
    public static final String THREAD_ALREADY_EXISTS = "Thread already exists";
}
