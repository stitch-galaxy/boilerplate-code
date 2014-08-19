/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.sg_rest_api.test;

import com.sg.sg_rest_api.utils.CustomMediaTypes;
import com.sg.sg_rest_api.test.configuration.WebApplicationUnitTestContext;
import com.sg.domain.service.SgService;
import com.sg.sg_rest_api.configuration.ServletContext;
import com.sg.constants.RequestPath;
import com.sg.constants.Roles;
import com.sg.constants.SigninStatus;
import com.sg.constants.SignupStatus;
import com.sg.dto.SigninDto;
import com.sg.dto.SignupDto;
import com.sg.dto.AccountDto;
import com.sg.domain.service.SgMailService;
import com.sg.domain.service.AuthToken;
import com.sg.domain.service.SgCryptoServiceImpl;
import com.sg.domain.service.exception.SgCryptoException;
import java.util.ArrayList;
import java.util.Arrays;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import org.codehaus.jackson.map.ObjectMapper;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

import static org.hamcrest.Matchers.*;
import org.joda.time.LocalDate;
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
public class SigninSignupControllerTest {

    private MockMvc mockMvc;

    @Autowired
    SgCryptoServiceImpl security;

    @Autowired
    private SgMailService mailServiceMock;
    
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
        Mockito.reset(mailServiceMock);
        

        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .build();
    }
    
    private static final LocalDate USER_BIRTH_DATE = LocalDate.parse("1985-01-28");
    
    @Test
    public void testUserSignupResentConfirmationEmail() throws Exception {
        testSignupResentConfirationEmail(RequestPath.REQUEST_SIGNUP_USER, Roles.ROLE_USER);
    }
    
    @Test
    public void testAdminSignupResentConfirmationEmail() throws Exception {
        testSignupResentConfirationEmail(RequestPath.REQUEST_SIGNUP_ADMIN_USER, Roles.ROLE_USER, Roles.ROLE_ADMIN);
    }

    private void testSignupResentConfirationEmail(String requestPath, String ... roles) throws Exception {
        SignupDto dto = new SignupDto();
        dto.setEmail(USER_EMAIL);
        dto.setUserFirstName(USER_FIRST_NAME);
        dto.setUserLastName(USER_LAST_NAME);
        dto.setUserBirthDate(USER_BIRTH_DATE);

        ObjectMapper mapper = new ObjectMapper();
        
        AccountDto accountDto = new AccountDto();
        accountDto.setId(USER_ID);
        accountDto.setUserBirthDate(USER_BIRTH_DATE);
        accountDto.setEmail(USER_EMAIL);
        accountDto.setEmailVerified(Boolean.FALSE);
        accountDto.setPassword(USER_PASSWORD);
        
        accountDto.setRoles(Arrays.asList(roles));

        when(serviceMock.getUserByEmail(dto.getEmail())).thenReturn(accountDto);

        mockMvc.perform(post(requestPath)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(CustomMediaTypes.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.status", is(SignupStatus.STATUS_CONFIRMATION_EMAIL_RESENT)));
        verify(serviceMock, times(1)).getUserByEmail(dto.getEmail());
        verify(mailServiceMock, times(1)).sendEmailVerificationEmail(anyString(), eq(USER_EMAIL));
        verifyNoMoreInteractions(serviceMock);
        verifyNoMoreInteractions(mailServiceMock);
    }
    
    @Test
    public void testUserSignupAlreadyRegistred() throws Exception {
        testSignupAlreadyRegistred(RequestPath.REQUEST_SIGNUP_USER, Roles.ROLE_ADMIN);
    }
    
    @Test
    public void testAdminSignupAlreadyRegistred() throws Exception {
        testSignupAlreadyRegistred(RequestPath.REQUEST_SIGNUP_ADMIN_USER, Roles.ROLE_USER, Roles.ROLE_ADMIN);
    }

    private void testSignupAlreadyRegistred(String requestPath, String ... roles) throws Exception {
        SignupDto dto = new SignupDto();
        dto.setEmail(USER_EMAIL);
        dto.setUserFirstName(USER_FIRST_NAME);
        dto.setUserLastName(USER_LAST_NAME);
        dto.setUserBirthDate(USER_BIRTH_DATE);

        ObjectMapper mapper = new ObjectMapper();
        
        AccountDto accountDto = new AccountDto();
        accountDto.setId(USER_ID);
        accountDto.setUserBirthDate(USER_BIRTH_DATE);
        accountDto.setEmail(USER_EMAIL);
        accountDto.setEmailVerified(Boolean.TRUE);
        accountDto.setPassword(USER_PASSWORD);
        accountDto.setRoles(Arrays.asList(roles));

        when(serviceMock.getUserByEmail(dto.getEmail())).thenReturn(accountDto);

        mockMvc.perform(post(requestPath)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(CustomMediaTypes.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.status", is(SignupStatus.STATUS_EMAIL_ALREADY_REGISTERED)));
        verify(serviceMock, times(1)).getUserByEmail(dto.getEmail());
        verifyNoMoreInteractions(serviceMock);
        verifyNoMoreInteractions(mailServiceMock);
    }
    private static final long USER_ID = 1L;
    
    @Test
    public void testUserSignupSuccessfully() throws Exception {
        testSignupSucceded(RequestPath.REQUEST_SIGNUP_USER, Roles.ROLE_USER);
    }
    
    @Test
    public void testAdminSignupSuccessfully() throws Exception {
        testSignupSucceded(RequestPath.REQUEST_SIGNUP_ADMIN_USER, Roles.ROLE_USER, Roles.ROLE_ADMIN);
    }

    private void testSignupSucceded(String requestPath, String ... roles) throws Exception {
        SignupDto dto = new SignupDto();
        dto.setEmail(USER_EMAIL);
        dto.setUserFirstName(USER_FIRST_NAME);
        dto.setUserLastName(USER_LAST_NAME);
        dto.setUserBirthDate(USER_BIRTH_DATE);

        ObjectMapper mapper = new ObjectMapper();

        when(serviceMock.getUserByEmail(dto.getEmail())).thenReturn(null);

        mockMvc.perform(post(requestPath)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(CustomMediaTypes.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.status", is(SignupStatus.STATUS_SUCCESS)));
        verify(serviceMock, times(1)).getUserByEmail(dto.getEmail());
        verify(serviceMock, times(1)).signup(dto, roles);
        verifyNoMoreInteractions(serviceMock);
    }
    
    public static final String USER_LAST_NAME = "Tarasov";
    public static final String USER_FIRST_NAME = "Evgeny";
    

    @Test
    public void testUserNotFound() throws Exception {

        SigninDto dto = new SigninDto();
        dto.setEmail(USER_EMAIL);
        dto.setPassword(USER_PASSWORD);

        ObjectMapper mapper = new ObjectMapper();

        when(serviceMock.getUserByEmail(dto.getEmail())).thenReturn(null);

        mockMvc.perform(post(RequestPath.REQUEST_SIGNIN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(CustomMediaTypes.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.status", is(SigninStatus.STATUS_USER_NOT_FOUND)));
        verify(serviceMock, times(1)).getUserByEmail(dto.getEmail());
        verifyNoMoreInteractions(serviceMock);
    }

    @Test
    public void testEmailNotVerified() throws Exception {
        SigninDto dto = new SigninDto();
        dto.setEmail(USER_EMAIL);
        dto.setPassword(USER_PASSWORD);

        ObjectMapper mapper = new ObjectMapper();

        AccountDto accountDto = new AccountDto();
        accountDto.setUserBirthDate(USER_BIRTH_DATE);
        accountDto.setEmail(USER_EMAIL);
        accountDto.setEmailVerified(Boolean.FALSE);
        accountDto.setPassword(USER_PASSWORD);
        List<String> roles = new ArrayList<String>();
        roles.add(Roles.ROLE_USER);
        roles.add(Roles.ROLE_ADMIN);
        accountDto.setRoles(roles);

        when(serviceMock.getUserByEmail(dto.getEmail())).thenReturn(accountDto);

        mockMvc.perform(post(RequestPath.REQUEST_SIGNIN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(CustomMediaTypes.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.status", is(SigninStatus.STATUS_EMAIL_NOT_VERIFIED)));
        verify(serviceMock, times(1)).getUserByEmail(dto.getEmail());
        verifyNoMoreInteractions(serviceMock);
    }

    @Test
    public void testWrongPassword() throws Exception {
        SigninDto dto = new SigninDto();
        dto.setEmail(USER_EMAIL);
        dto.setPassword(USER_WRONG_PASSWORD);

        ObjectMapper mapper = new ObjectMapper();

        AccountDto accountDto = new AccountDto();
        accountDto.setUserBirthDate(USER_BIRTH_DATE);
        accountDto.setEmail(USER_EMAIL);
        accountDto.setEmailVerified(Boolean.TRUE);
        accountDto.setPassword(USER_PASSWORD);
        List<String> roles = new ArrayList<String>();
        roles.add(Roles.ROLE_USER);
        roles.add(Roles.ROLE_ADMIN);
        accountDto.setRoles(roles);

        when(serviceMock.getUserByEmail(dto.getEmail())).thenReturn(accountDto);

        mockMvc.perform(post(RequestPath.REQUEST_SIGNIN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(CustomMediaTypes.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.status", is(SigninStatus.STATUS_WRONG_PASSWORD)));
        verify(serviceMock, times(1)).getUserByEmail(dto.getEmail());
        verifyNoMoreInteractions(serviceMock);
    }

    @Test
    public void testSuccessSignin() throws Exception {
        SigninDto dto = new SigninDto();
        dto.setEmail(USER_EMAIL);
        dto.setPassword(USER_PASSWORD);

        ObjectMapper mapper = new ObjectMapper();

        AccountDto accountDto = new AccountDto();
        accountDto.setId(USER_ID);
        accountDto.setUserBirthDate(USER_BIRTH_DATE);
        accountDto.setEmail(USER_EMAIL);
        accountDto.setEmailVerified(Boolean.TRUE);
        accountDto.setPassword(USER_PASSWORD);
        List<String> roles = new ArrayList<String>();
        roles.add(Roles.ROLE_USER);
        roles.add(Roles.ROLE_ADMIN);
        accountDto.setRoles(roles);

        when(serviceMock.getUserByEmail(dto.getEmail())).thenReturn(accountDto);

        TokenMatcher tokenMatcher = new TokenMatcher(security);
        tokenMatcher.setUserId(USER_ID);
        List<String> authorities = new ArrayList<String>();
        for (String r : roles) {
            authorities.add(Roles.ROLE_AUTHORITY_PREFIX + r);
        }
        tokenMatcher.setAuthorities(authorities);
        
        mockMvc.perform(post(RequestPath.REQUEST_SIGNIN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(CustomMediaTypes.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.status", is(SigninStatus.STATUS_SUCCESS)))
                .andExpect(jsonPath("$.authToken", tokenMatcher));
        verify(serviceMock, times(1)).getUserByEmail(dto.getEmail());
        verifyNoMoreInteractions(serviceMock);
    }

    private static final String USER_PASSWORD = "secret";
    private static final String USER_WRONG_PASSWORD = "bad secret";
    private static final String USER_EMAIL = "test@example.com";
    
    private static class TokenMatcher extends BaseMatcher<String>
    {
        private SgCryptoServiceImpl security;
        
        private Long userId;
        private List<String> authorities;
        
        private String reason;
        
        public TokenMatcher(SgCryptoServiceImpl security)
        {
            this.security = security;
        }

        public boolean matches(Object item) {
            if (item == null || !(item instanceof String))
                return false;
            
            String encryptedToken = (String) item;
            AuthToken token;
            try {
                token = security.getTokenFromString(encryptedToken);
            } catch (SgCryptoException e) {
                reason = e.getMessage();
                return false;
            }
            
            if (!userId.equals(token.getUserId()))
            {
                reason = "Non expected userId";
                return false;
            }
            if (!authorities.equals(token.getAuthorities()))
            {
                reason = "Non expected authorities";
                return false;
            }
            return true;
        }

        public void describeTo(Description description) {
            description.appendText(reason);
        }

        /**
         * @return the authorities
         */
        public List<String> getAuthorities() {
            return authorities;
        }

        /**
         * @param authorities the authorities to set
         */
        public void setAuthorities(List<String> authorities) {
            this.authorities = authorities;
        }

        /**
         * @return the userId
         */
        public Long getUserId() {
            return userId;
        }

        /**
         * @param userId the userId to set
         */
        public void setUserId(Long userId) {
            this.userId = userId;
        }
        
    }
}
