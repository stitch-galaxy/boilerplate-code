/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.sg_rest_api.test;

import com.sg.constants.CompleteSignupStatus;
import com.sg.constants.CustomHttpHeaders;
import com.sg.sg_rest_api.utils.CustomMediaTypes;
import com.sg.sg_rest_api.test.configuration.WebApplicationUnitTestContext;
import com.sg.domain.service.SgService;
import com.sg.sg_rest_api.configuration.ServletContext;
import com.sg.constants.RequestPath;
import com.sg.constants.Roles;
import com.sg.constants.SigninStatus;
import com.sg.constants.SignupStatus;
import com.sg.constants.TokenExpirationType;
import com.sg.dto.SignupDto;
import com.sg.dto.AccountDto;
import com.sg.domain.service.SgMailService;
import com.sg.domain.service.AuthToken;
import com.sg.domain.service.SgCryptoService;
import com.sg.domain.service.exception.SgAccountNotFoundException;
import com.sg.domain.service.exception.SgEmailNonVerifiedException;
import com.sg.domain.service.exception.SgInvalidPasswordException;
import com.sg.domain.service.exception.SgSignupAlreadyCompletedException;
import com.sg.domain.service.exception.SgSignupForRegisteredButNonVerifiedEmailException;
import com.sg.dto.CompleteSignupDto;
import com.sg.dto.SigninDto;
import java.util.Arrays;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import org.codehaus.jackson.map.ObjectMapper;

import static org.hamcrest.Matchers.*;
import org.joda.time.LocalDate;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.mockito.Mockito;
import static org.mockito.Mockito.*;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
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
    SgCryptoService security;

    @Autowired
    private SgMailService mailServiceMock;
    
    @Autowired
    private SgCryptoService cryptoMock;

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
        Mockito.reset(cryptoMock);

        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .build();
    }

    private static final String SECURE_TOKEN_STRING = "secure token";
    private static final Long ACCOUNT_ID = 1L;
    private static final String USER_PASSWORD = "secret";
    private static final String USER_WRONG_PASSWORD = "bad secret";
    private static final String USER_EMAIL = "test@example.com";
    private static final String USER_LAST_NAME = "Tarasov";
    private static final String USER_FIRST_NAME = "Evgeny";
    private static final LocalDate USER_BIRTH_DATE = LocalDate.parse("1985-01-28");
    private static final SignupDto signupDto;
    private static final AccountDto nonVerifiedUserAccountDto;
    private static final AccountDto nonVerifiedAdminUserAccountDto;
    private static final AccountDto verifiedUserAccountDto;
    private static final CompleteSignupDto completeSignupDto;
    private static final SigninDto signinDto;
    

    static {
        signupDto = new SignupDto();
        signupDto.setEmail(USER_EMAIL);
        signupDto.setUserFirstName(USER_FIRST_NAME);
        signupDto.setUserLastName(USER_LAST_NAME);
        signupDto.setUserBirthDate(USER_BIRTH_DATE);

        nonVerifiedUserAccountDto = new AccountDto();
        nonVerifiedUserAccountDto.setEmail(USER_EMAIL);
        nonVerifiedUserAccountDto.setEmailVerified(Boolean.FALSE);
        nonVerifiedUserAccountDto.setId(ACCOUNT_ID);
        nonVerifiedUserAccountDto.setRoles(Arrays.asList(new String[]{Roles.ROLE_USER}));
        nonVerifiedUserAccountDto.setUserBirthDate(USER_BIRTH_DATE);
        nonVerifiedUserAccountDto.setUserFirstName(USER_FIRST_NAME);
        nonVerifiedUserAccountDto.setUserLastName(USER_LAST_NAME);
        
        verifiedUserAccountDto = new AccountDto();
        verifiedUserAccountDto.setEmail(USER_EMAIL);
        verifiedUserAccountDto.setEmailVerified(Boolean.TRUE);
        verifiedUserAccountDto.setId(ACCOUNT_ID);
        verifiedUserAccountDto.setRoles(Arrays.asList(new String[]{Roles.ROLE_USER}));
        verifiedUserAccountDto.setUserBirthDate(USER_BIRTH_DATE);
        verifiedUserAccountDto.setUserFirstName(USER_FIRST_NAME);
        verifiedUserAccountDto.setUserLastName(USER_LAST_NAME);
        
        
        
        nonVerifiedAdminUserAccountDto = new AccountDto();
        nonVerifiedAdminUserAccountDto.setEmail(USER_EMAIL);
        nonVerifiedAdminUserAccountDto.setEmailVerified(Boolean.FALSE);
        nonVerifiedAdminUserAccountDto.setId(ACCOUNT_ID);
        nonVerifiedAdminUserAccountDto.setRoles(Arrays.asList(new String[]{Roles.ROLE_USER, Roles.ROLE_ADMIN}));
        nonVerifiedAdminUserAccountDto.setUserBirthDate(USER_BIRTH_DATE);
        nonVerifiedAdminUserAccountDto.setUserFirstName(USER_FIRST_NAME);
        nonVerifiedAdminUserAccountDto.setUserLastName(USER_LAST_NAME);
        
        completeSignupDto = new CompleteSignupDto();
        completeSignupDto.setPassword(USER_PASSWORD);
        
        signinDto = new SigninDto();
        signinDto.setEmail(USER_EMAIL);
        signinDto.setPassword(USER_PASSWORD);
    }

    @Test
    public void testUserSignupResentConfirmationEmail() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        doThrow(new SgSignupForRegisteredButNonVerifiedEmailException(signupDto.getEmail())).when(serviceMock).signupUser(signupDto);
        when(serviceMock.getAccountIdByRegistrationEmail(signupDto.getEmail())).thenReturn(nonVerifiedUserAccountDto.getId());
        when(serviceMock.getAccountInfo(nonVerifiedUserAccountDto.getId())).thenReturn(nonVerifiedUserAccountDto);
        when(cryptoMock.encryptSecurityToken(org.mockito.Matchers.any(AuthToken.class))).thenReturn(SECURE_TOKEN_STRING);

        mockMvc.perform(post(RequestPath.REQUEST_SIGNUP_USER)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(signupDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(CustomMediaTypes.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.status", is(SignupStatus.STATUS_CONFIRMATION_EMAIL_RESENT)));

        verify(serviceMock, times(1)).signupUser(signupDto);
        verify(serviceMock, times(1)).getAccountIdByRegistrationEmail(signupDto.getEmail());
        verify(serviceMock, times(1)).getAccountInfo(ACCOUNT_ID);
        verify(cryptoMock, times(1)).encryptSecurityToken(org.mockito.Matchers.any(AuthToken.class));

        verify(mailServiceMock, times(1)).sendEmailVerificationEmail(eq(SECURE_TOKEN_STRING), eq(USER_EMAIL));
        verifyNoMoreInteractions(serviceMock);
        verifyNoMoreInteractions(cryptoMock);
        verifyNoMoreInteractions(mailServiceMock);
    }
    
    @Test
    public void testAdminSignupResentConfirmationEmail() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        doThrow(new SgSignupForRegisteredButNonVerifiedEmailException(signupDto.getEmail())).when(serviceMock).signupAdmin(signupDto);
        when(serviceMock.getAccountIdByRegistrationEmail(signupDto.getEmail())).thenReturn(nonVerifiedAdminUserAccountDto.getId());
        when(serviceMock.getAccountInfo(nonVerifiedAdminUserAccountDto.getId())).thenReturn(nonVerifiedAdminUserAccountDto);
        when(cryptoMock.encryptSecurityToken(org.mockito.Matchers.any(AuthToken.class))).thenReturn(SECURE_TOKEN_STRING);

        mockMvc.perform(post(RequestPath.REQUEST_SIGNUP_ADMIN_USER)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(signupDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(CustomMediaTypes.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.status", is(SignupStatus.STATUS_CONFIRMATION_EMAIL_RESENT)));

        verify(serviceMock, times(1)).signupAdmin(signupDto);
        verify(serviceMock, times(1)).getAccountIdByRegistrationEmail(signupDto.getEmail());
        verify(serviceMock, times(1)).getAccountInfo(ACCOUNT_ID);
        verify(cryptoMock, times(1)).encryptSecurityToken(org.mockito.Matchers.any(AuthToken.class));

        verify(mailServiceMock, times(1)).sendEmailVerificationEmail(eq(SECURE_TOKEN_STRING), eq(USER_EMAIL));
        verifyNoMoreInteractions(serviceMock);
        verifyNoMoreInteractions(cryptoMock);
        verifyNoMoreInteractions(mailServiceMock);
    }
    
    @Test
    public void testUserSignupAlreadyRegistered() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        doThrow(new SgSignupAlreadyCompletedException(USER_EMAIL)).when(serviceMock).signupUser(signupDto);

        mockMvc.perform(post(RequestPath.REQUEST_SIGNUP_USER)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(signupDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(CustomMediaTypes.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.status", is(SignupStatus.STATUS_EMAIL_ALREADY_REGISTERED)));

        verify(serviceMock, times(1)).signupUser(signupDto);
        verifyNoMoreInteractions(serviceMock);
        verifyNoMoreInteractions(cryptoMock);
        verifyNoMoreInteractions(mailServiceMock);
    }
    
    @Test
    public void testAdminSignupAlreadyRegistered() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        doThrow(new SgSignupAlreadyCompletedException(USER_EMAIL)).when(serviceMock).signupAdmin(signupDto);

        mockMvc.perform(post(RequestPath.REQUEST_SIGNUP_ADMIN_USER)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(signupDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(CustomMediaTypes.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.status", is(SignupStatus.STATUS_EMAIL_ALREADY_REGISTERED)));

        verify(serviceMock, times(1)).signupAdmin(signupDto);
        verifyNoMoreInteractions(serviceMock);
        verifyNoMoreInteractions(cryptoMock);
        verifyNoMoreInteractions(mailServiceMock);
    }
    
    @Test
    public void testUserSignupSuccessfully() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        when(serviceMock.getAccountIdByRegistrationEmail(signupDto.getEmail())).thenReturn(nonVerifiedUserAccountDto.getId());
        when(serviceMock.getAccountInfo(nonVerifiedUserAccountDto.getId())).thenReturn(nonVerifiedUserAccountDto);
        when(cryptoMock.encryptSecurityToken(org.mockito.Matchers.any(AuthToken.class))).thenReturn(SECURE_TOKEN_STRING);

        mockMvc.perform(post(RequestPath.REQUEST_SIGNUP_USER)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(signupDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(CustomMediaTypes.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.status", is(SignupStatus.STATUS_SUCCESS)));

        verify(serviceMock, times(1)).signupUser(signupDto);
        verify(serviceMock, times(1)).getAccountIdByRegistrationEmail(signupDto.getEmail());
        verify(serviceMock, times(1)).getAccountInfo(ACCOUNT_ID);
        verify(cryptoMock, times(1)).encryptSecurityToken(org.mockito.Matchers.any(AuthToken.class));

        verify(mailServiceMock, times(1)).sendEmailVerificationEmail(eq(SECURE_TOKEN_STRING), eq(USER_EMAIL));
        verifyNoMoreInteractions(serviceMock);
        verifyNoMoreInteractions(cryptoMock);
        verifyNoMoreInteractions(mailServiceMock);
    }
    
    @Test
    public void testAdminSignupSuccessfully() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        when(serviceMock.getAccountIdByRegistrationEmail(signupDto.getEmail())).thenReturn(nonVerifiedAdminUserAccountDto.getId());
        when(serviceMock.getAccountInfo(nonVerifiedAdminUserAccountDto.getId())).thenReturn(nonVerifiedAdminUserAccountDto);
        when(cryptoMock.encryptSecurityToken(org.mockito.Matchers.any(AuthToken.class))).thenReturn(SECURE_TOKEN_STRING);

        mockMvc.perform(post(RequestPath.REQUEST_SIGNUP_ADMIN_USER)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(signupDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(CustomMediaTypes.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.status", is(SignupStatus.STATUS_SUCCESS)));

        verify(serviceMock, times(1)).signupAdmin(signupDto);
        verify(serviceMock, times(1)).getAccountIdByRegistrationEmail(signupDto.getEmail());
        verify(serviceMock, times(1)).getAccountInfo(ACCOUNT_ID);
        verify(cryptoMock, times(1)).encryptSecurityToken(org.mockito.Matchers.any(AuthToken.class));

        verify(mailServiceMock, times(1)).sendEmailVerificationEmail(eq(SECURE_TOKEN_STRING), eq(USER_EMAIL));
        verifyNoMoreInteractions(serviceMock);
        verifyNoMoreInteractions(cryptoMock);
        verifyNoMoreInteractions(mailServiceMock);
    }
    
    @BeforeClass
    public static void setup()
    {
        SecurityContextHolder.getContext().setAuthentication(
		new UsernamePasswordAuthenticationToken(ACCOUNT_ID, null));
    }
    
    @AfterClass
    public static void tearDown()
    {
        SecurityContextHolder.getContext().setAuthentication(null);
    }
    
    @Test
    public void testCompleteSignupAccountNotFound() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        doThrow(new SgAccountNotFoundException(ACCOUNT_ID)).when(serviceMock).completeSignup(ACCOUNT_ID, completeSignupDto);
        
        mockMvc.perform(post(RequestPath.REQUEST_COMPLETE_SIGNUP)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(completeSignupDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(CustomMediaTypes.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.status", is(CompleteSignupStatus.STATUS_ACCOUNT_NOT_FOUND)));

        verify(serviceMock, times(1)).completeSignup(ACCOUNT_ID, completeSignupDto);

        verifyNoMoreInteractions(serviceMock);
        verifyNoMoreInteractions(cryptoMock);
        verifyNoMoreInteractions(mailServiceMock);
    }
    
    @Test
    public void testCompleteSignupAlreadyCompleted() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        doThrow(new SgSignupAlreadyCompletedException(USER_EMAIL)).when(serviceMock).completeSignup(ACCOUNT_ID, completeSignupDto);
        
        mockMvc.perform(post(RequestPath.REQUEST_COMPLETE_SIGNUP)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(completeSignupDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(CustomMediaTypes.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.status", is(CompleteSignupStatus.STATUS_ALREADY_COMPLETED)));

        verify(serviceMock, times(1)).completeSignup(ACCOUNT_ID, completeSignupDto);

        verifyNoMoreInteractions(serviceMock);
        verifyNoMoreInteractions(cryptoMock);
        verifyNoMoreInteractions(mailServiceMock);
    }
    
    @Test
    public void testCompleteSignupSuccessfully() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        mockMvc.perform(post(RequestPath.REQUEST_COMPLETE_SIGNUP)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(completeSignupDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(CustomMediaTypes.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.status", is(CompleteSignupStatus.STATUS_SUCCESS)));

        verify(serviceMock, times(1)).completeSignup(ACCOUNT_ID, completeSignupDto);

        verifyNoMoreInteractions(serviceMock);
        verifyNoMoreInteractions(cryptoMock);
        verifyNoMoreInteractions(mailServiceMock);
    }
    
    //, ;

    @Test
    public void testSigninAccountNotFound() throws Exception
    {
        ObjectMapper mapper = new ObjectMapper();

        doThrow(new SgAccountNotFoundException(signinDto.getEmail())).when(serviceMock).signIn(signinDto);
        
        mockMvc.perform(post(RequestPath.REQUEST_SIGNIN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(signinDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(CustomMediaTypes.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.status", is(SigninStatus.STATUS_USER_NOT_FOUND)));

        verify(serviceMock, times(1)).signIn(signinDto);

        verifyNoMoreInteractions(serviceMock);
        verifyNoMoreInteractions(cryptoMock);
        verifyNoMoreInteractions(mailServiceMock);
    }
    
    @Test
    public void testSigninInvalidPassword() throws Exception
    {
        ObjectMapper mapper = new ObjectMapper();

        doThrow(new SgInvalidPasswordException()).when(serviceMock).signIn(signinDto);
        
        mockMvc.perform(post(RequestPath.REQUEST_SIGNIN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(signinDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(CustomMediaTypes.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.status", is(SigninStatus.STATUS_WRONG_PASSWORD)));

        verify(serviceMock, times(1)).signIn(signinDto);

        verifyNoMoreInteractions(serviceMock);
        verifyNoMoreInteractions(cryptoMock);
        verifyNoMoreInteractions(mailServiceMock);
    }
    
    @Test
    public void testSigninEmailNonVerified() throws Exception
    {
        ObjectMapper mapper = new ObjectMapper();

        doThrow(new SgEmailNonVerifiedException(signinDto.getEmail())).when(serviceMock).signIn(signinDto);
        
        mockMvc.perform(post(RequestPath.REQUEST_SIGNIN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(signinDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(CustomMediaTypes.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.status", is(SigninStatus.STATUS_EMAIL_NOT_VERIFIED)));

        verify(serviceMock, times(1)).signIn(signinDto);

        verifyNoMoreInteractions(serviceMock);
        verifyNoMoreInteractions(cryptoMock);
        verifyNoMoreInteractions(mailServiceMock);
    }
    
    @Test
    public void testSigninSuccessfully() throws Exception
    {
        ObjectMapper mapper = new ObjectMapper();
        
        when(serviceMock.getAccountIdByRegistrationEmail(signinDto.getEmail())).thenReturn(verifiedUserAccountDto.getId());
        when(serviceMock.getAccountInfo(verifiedUserAccountDto.getId())).thenReturn(verifiedUserAccountDto);
        when(cryptoMock.encryptSecurityToken(org.mockito.Matchers.any(AuthToken.class))).thenReturn(SECURE_TOKEN_STRING);

        mockMvc.perform(post(RequestPath.REQUEST_SIGNIN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(signinDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(CustomMediaTypes.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.status", is(SigninStatus.STATUS_SUCCESS)))
                .andExpect(header().string(CustomHttpHeaders.X_AUTH_TOKEN, is(SECURE_TOKEN_STRING)));

        verify(serviceMock, times(1)).signIn(signinDto);
        verify(serviceMock, times(1)).getAccountIdByRegistrationEmail(signinDto.getEmail());
        verify(serviceMock, times(1)).getAccountInfo(ACCOUNT_ID);
        verify(cryptoMock, times(1)).encryptSecurityToken(org.mockito.Matchers.any(AuthToken.class));

        verifyNoMoreInteractions(serviceMock);
        verifyNoMoreInteractions(cryptoMock);
        verifyNoMoreInteractions(mailServiceMock);
    }
    
}
