/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.sg_rest_api.test;

import com.sg.sg_rest_api.utils.CustomMediaTypes;
import com.sg.domain.service.SgService;
import com.sg.sg_rest_api.configuration.ServletContext;
import com.sg.constants.RequestPath;
import com.sg.domain.service.exception.SgDataValidationException;
import com.sg.sg_rest_api.test.configuration.WebApplicationUnitTestContext;
import java.util.HashSet;
import java.util.Set;
import javax.servlet.http.HttpServletResponse;
import org.codehaus.jackson.map.ObjectMapper;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import org.springframework.beans.factory.annotation.Autowired;
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
@ContextConfiguration(classes = {WebApplicationUnitTestContext.class, ServletContext.class})
public class DataValidationTest {

    private MockMvc mockMvc;

    @Autowired
    ObjectMapper jacksonObjectMapper;
    
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
    
    private static final String TEST_FIELD = "TEST_FIELD";
    
    @Test
    public void testServiceLayerValidationError() throws Exception {
        Set<String> fieldErrors = new HashSet<String>();
        fieldErrors.add(TEST_FIELD);
        SgDataValidationException ex = new SgDataValidationException();
        ex.setFieldErrors(fieldErrors);
        
        doThrow(ex).when(serviceMock).ping();
        
        mockMvc.perform(get(RequestPath.REQUEST_PING))
                .andExpect(status().is(HttpServletResponse.SC_BAD_REQUEST))
                .andExpect(content().contentType(CustomMediaTypes.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.fieldErrors", hasSize(1)))
                .andExpect((jsonPath("$.fieldErrors", containsInAnyOrder(
                        TEST_FIELD))));
        verify(serviceMock, times(1)).ping();
        verifyNoMoreInteractions(serviceMock);
    }
}
