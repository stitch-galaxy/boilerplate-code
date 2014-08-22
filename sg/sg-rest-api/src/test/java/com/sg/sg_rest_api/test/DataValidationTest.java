/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.sg_rest_api.test;

import com.sg.constants.OperationStatus;
import com.sg.sg_rest_api.utils.CustomMediaTypes;
import com.sg.domain.service.SgService;
import com.sg.sg_rest_api.configuration.ServletContext;
import com.sg.constants.RequestPath;
import com.sg.constants.SignupStatus;
import com.sg.domain.service.exception.SgDataValidationException;
import com.sg.dto.request.CompleteSignupDto;
import com.sg.dto.request.SigninDto;
import com.sg.dto.request.SignupDto;
import com.sg.dto.request.ThreadDeleteDto;
import com.sg.dto.request.ThreadCreateDto;
import com.sg.dto.request.ThreadUpdateDto;
import com.sg.sg_rest_api.test.configuration.WebApplicationUnitTestContext;
import java.util.HashSet;
import java.util.Set;
import javax.servlet.http.HttpServletResponse;
import org.codehaus.jackson.map.ObjectMapper;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import org.joda.time.LocalDate;
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
@ContextConfiguration(classes = {WebApplicationUnitTestContext.class, ServletContext.class})
public class DataValidationTest {

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
    private static final String TEST_FIELD = "TEST_FIELD";
    
    
    private static final String INVALID_EMAIL = "abc";
    private static final String INVALID_USER_LAST_NAME = "";
    private static final String INVALID_USER_FIRST_NAME = "";
    private static final LocalDate INVALID_USER_BIRTH_DATE = null;
    private static final String INVALID_PASSWORD = "badpassword";
    private static final String EMPTY_PASSWORD = "";
    private static final String INVALID_THREAD_CODE = "";
    

    @Test
    public void testSignupUserDtoError() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        
        SignupDto dto = new SignupDto();
        dto.setEmail(INVALID_EMAIL);
        dto.setUserBirthDate(INVALID_USER_BIRTH_DATE);
        dto.setUserFirstName(INVALID_USER_FIRST_NAME);
        dto.setUserLastName(INVALID_USER_LAST_NAME);
        
        mockMvc.perform(post(RequestPath.REQUEST_SIGNUP_USER)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(dto)))
                .andExpect(status().is(HttpServletResponse.SC_BAD_REQUEST))
                .andExpect(content().contentType(CustomMediaTypes.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.fieldErrors", hasSize(4)))
                .andExpect((jsonPath("$.fieldErrors", containsInAnyOrder(
                        SignupDto.FIELD_SIGNUP_EMAIL, 
                        SignupDto.FIELD_SIGNUP_USER_BIRTH_DATE, 
                        SignupDto.FIELD_SIGNUP_USER_FIRST_NAME, 
                        SignupDto.FIELD_SIGNUP_USER_LAST_NAME
                ))));

        verifyNoMoreInteractions(serviceMock);
    }
    
    @Test
    public void testSignupAdminDtoError() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        
        SignupDto dto = new SignupDto();
        dto.setEmail(INVALID_EMAIL);
        dto.setUserBirthDate(INVALID_USER_BIRTH_DATE);
        dto.setUserFirstName(INVALID_USER_FIRST_NAME);
        dto.setUserLastName(INVALID_USER_LAST_NAME);
        
        mockMvc.perform(post(RequestPath.REQUEST_SIGNUP_ADMIN_USER)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(dto)))
                .andExpect(status().is(HttpServletResponse.SC_BAD_REQUEST))
                .andExpect(content().contentType(CustomMediaTypes.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.fieldErrors", hasSize(4)))
                .andExpect((jsonPath("$.fieldErrors", containsInAnyOrder(
                        SignupDto.FIELD_SIGNUP_EMAIL, 
                        SignupDto.FIELD_SIGNUP_USER_BIRTH_DATE, 
                        SignupDto.FIELD_SIGNUP_USER_FIRST_NAME, 
                        SignupDto.FIELD_SIGNUP_USER_LAST_NAME
                ))));

        verifyNoMoreInteractions(serviceMock);
    }
    
    @Test
    public void testCompleteSignupDtoError() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        
        CompleteSignupDto dto = new CompleteSignupDto();
        dto.setPassword(INVALID_PASSWORD);
        
        mockMvc.perform(post(RequestPath.REQUEST_COMPLETE_SIGNUP)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(dto)))
                .andExpect(status().is(HttpServletResponse.SC_BAD_REQUEST))
                .andExpect(content().contentType(CustomMediaTypes.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.fieldErrors", hasSize(1)))
                .andExpect((jsonPath("$.fieldErrors", containsInAnyOrder(
                        CompleteSignupDto.FIELD_COMPLETE_SIGNUP_PASSWORD 
                ))));

        verifyNoMoreInteractions(serviceMock);
    }
    
    @Test
    public void testSigninDtoError() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        
        SigninDto dto = new SigninDto();
        dto.setEmail(INVALID_EMAIL);
        dto.setPassword(EMPTY_PASSWORD);
        
        mockMvc.perform(post(RequestPath.REQUEST_SIGNIN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(dto)))
                .andExpect(status().is(HttpServletResponse.SC_BAD_REQUEST))
                .andExpect(content().contentType(CustomMediaTypes.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.fieldErrors", hasSize(2)))
                .andExpect((jsonPath("$.fieldErrors", containsInAnyOrder(
                        SigninDto.FIELD_SIGNIN_EMAIL, 
                        SigninDto.FIELD_SIGNIN_PASSWORD
                ))));

        verifyNoMoreInteractions(serviceMock);
    }
    
    @Test
    public void testThreadCreateDtoError() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        
        ThreadCreateDto dto = new ThreadCreateDto();
        dto.setCode(INVALID_THREAD_CODE);
        
        mockMvc.perform(post(RequestPath.REQUEST_THREAD_ADD)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(dto)))
                .andExpect(status().is(HttpServletResponse.SC_BAD_REQUEST))
                .andExpect(content().contentType(CustomMediaTypes.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.fieldErrors", hasSize(1)))
                .andExpect((jsonPath("$.fieldErrors", containsInAnyOrder(
                        ThreadCreateDto.FIELD_THREAD_CODE
                ))));

        verifyNoMoreInteractions(serviceMock);
    }
    
    @Test
    public void testThreadDeleteDtoError() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        
        ThreadDeleteDto dto = new ThreadDeleteDto();
        dto.setCode(INVALID_THREAD_CODE);
        
        mockMvc.perform(post(RequestPath.REQUEST_THREAD_DELETE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(dto)))
                .andExpect(status().is(HttpServletResponse.SC_BAD_REQUEST))
                .andExpect(content().contentType(CustomMediaTypes.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.fieldErrors", hasSize(1)))
                .andExpect((jsonPath("$.fieldErrors", containsInAnyOrder(
                        ThreadDeleteDto.FIELD_THREAD_CODE
                ))));

        verifyNoMoreInteractions(serviceMock);
    }
    
    @Test
    public void testThreadUpdateDtoError() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        
        ThreadUpdateDto dto = new ThreadUpdateDto();
        dto.setRefCode(INVALID_THREAD_CODE);
        dto.setCode(INVALID_THREAD_CODE);
        
        mockMvc.perform(post(RequestPath.REQUEST_THREAD_UPDATE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(dto)))
                .andExpect(status().is(HttpServletResponse.SC_BAD_REQUEST))
                .andExpect(content().contentType(CustomMediaTypes.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.fieldErrors", hasSize(2)))
                .andExpect((jsonPath("$.fieldErrors", containsInAnyOrder(
                        ThreadUpdateDto.FIELD_THREAD_CODE,
                        ThreadUpdateDto.FIELD_THREAD_REF_CODE
                ))));

        verifyNoMoreInteractions(serviceMock);
    }
    
}
