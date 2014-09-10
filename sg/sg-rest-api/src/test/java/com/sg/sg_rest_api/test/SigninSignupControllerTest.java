/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.sg_rest_api.test;

import com.sg.constants.CompleteSignupStatus;
import com.sg.constants.CustomHttpHeaders;
import com.sg.constants.InstallStatus;
import com.sg.sg_rest_api.utils.CustomMediaTypes;
import com.sg.sg_rest_api.test.configuration.WebApplicationUnitTestContext;
import com.sg.domain.service.SgService;
import com.sg.sg_rest_api.configuration.ServletContext;
import com.sg.constants.RequestPath;
import com.sg.constants.Roles;
import com.sg.constants.SigninStatus;
import com.sg.constants.SignupStatus;
import com.sg.constants.TokenExpirationType;
import com.sg.dto.request.SignupDto;
import com.sg.dto.response.AccountPrincipalDto;
import com.sg.domain.service.SgMailService;
import com.sg.domain.service.AuthToken;
import com.sg.domain.service.SgCryptoService;
import com.sg.domain.service.exception.SgAccountNotFoundException;
import com.sg.domain.service.exception.SgEmailNonVerifiedException;
import com.sg.domain.service.exception.SgInstallationAlreadyCompletedException;
import com.sg.domain.service.exception.SgInvalidPasswordException;
import com.sg.domain.service.exception.SgSignupAlreadyCompletedException;
import com.sg.domain.service.exception.SgSignupForRegisteredButNonVerifiedEmailException;
import com.sg.dto.request.CompleteSignupDto;
import com.sg.dto.request.SigninDto;
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
    private ObjectMapper jacksonObjectMapper;

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
    private static final String USER_PASSWORD = "1GoodPassword!";
    private static final String USER_WRONG_PASSWORD = "badpwd";
    private static final String USER_EMAIL = "test@example.com";
    private static final String USER_LAST_NAME = "Tarasov";
    private static final String USER_FIRST_NAME = "Evgeny";
    private static final LocalDate USER_BIRTH_DATE = LocalDate.parse("1985-01-28");
    private static final SignupDto signupDto;
    private static final AccountPrincipalDto nonVerifiedUserAccountPrincipalDto;
    private static final AccountPrincipalDto nonVerifiedAdminUserAccountPrincipalDto;
    private static final AccountPrincipalDto verifiedUserAccountPrincipalDto;
    private static final CompleteSignupDto completeSignupDto;
    private static final SigninDto signinDto;

    static {
        signupDto = new SignupDto();
        signupDto.setEmail(USER_EMAIL);
        signupDto.setUserFirstName(USER_FIRST_NAME);
        signupDto.setUserLastName(USER_LAST_NAME);

        nonVerifiedUserAccountPrincipalDto = new AccountPrincipalDto();
        nonVerifiedUserAccountPrincipalDto.setEmailVerified(Boolean.FALSE);
        nonVerifiedUserAccountPrincipalDto.setId(ACCOUNT_ID);
        nonVerifiedUserAccountPrincipalDto.setRoles(Arrays.asList(new String[]{Roles.ROLE_USER}));

        verifiedUserAccountPrincipalDto = new AccountPrincipalDto();
        verifiedUserAccountPrincipalDto.setEmailVerified(Boolean.TRUE);
        verifiedUserAccountPrincipalDto.setId(ACCOUNT_ID);
        verifiedUserAccountPrincipalDto.setRoles(Arrays.asList(new String[]{Roles.ROLE_USER}));

        nonVerifiedAdminUserAccountPrincipalDto = new AccountPrincipalDto();
        nonVerifiedAdminUserAccountPrincipalDto.setEmailVerified(Boolean.FALSE);
        nonVerifiedAdminUserAccountPrincipalDto.setId(ACCOUNT_ID);
        nonVerifiedAdminUserAccountPrincipalDto.setRoles(Arrays.asList(new String[]{Roles.ROLE_USER, Roles.ROLE_ADMIN}));

        completeSignupDto = new CompleteSignupDto();
        completeSignupDto.setPassword(USER_PASSWORD);

        signinDto = new SigninDto();
        signinDto.setEmail(USER_EMAIL);
        signinDto.setPassword(USER_PASSWORD);
    }

    @Test
    public void testInstallAlreadyCompleted() throws Exception {
        doThrow(new SgInstallationAlreadyCompletedException()).when(serviceMock).install();

        mockMvc.perform(get(RequestPath.REQUEST_INSTALL))
                .andExpect(status().isOk())
                .andExpect(content().contentType(CustomMediaTypes.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.status", is(InstallStatus.STATUS_ALREADY_COMPLETED)));

        verify(serviceMock, times(1)).install();
        verifyNoMoreInteractions(serviceMock);

    }

    @Test
    public void testInstall() throws Exception {
        mockMvc.perform(get(RequestPath.REQUEST_INSTALL))
                .andExpect(status().isOk())
                .andExpect(content().contentType(CustomMediaTypes.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.status", is(InstallStatus.STATUS_SUCCESS)));

        verify(serviceMock, times(1)).install();
        verifyNoMoreInteractions(serviceMock);
    }

    @Test
    public void testUserSignupResentConfirmationEmail() throws Exception {
        doThrow(new SgSignupForRegisteredButNonVerifiedEmailException(signupDto.getEmail())).when(serviceMock).signupUser(signupDto);
        when(serviceMock.getAccountPrincipal(signupDto.getEmail())).thenReturn(nonVerifiedUserAccountPrincipalDto);
        when(cryptoMock.encryptSecurityToken(org.mockito.Matchers.any(AuthToken.class))).thenReturn(SECURE_TOKEN_STRING);

        mockMvc.perform(post(RequestPath.REQUEST_SIGNUP_USER)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jacksonObjectMapper.writeValueAsString(signupDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(CustomMediaTypes.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.status", is(SignupStatus.STATUS_CONFIRMATION_EMAIL_RESENT)));

        verify(serviceMock, times(1)).signupUser(signupDto);
        verify(serviceMock, times(1)).getAccountPrincipal(signupDto.getEmail());
        verify(cryptoMock, times(1)).encryptSecurityToken(org.mockito.Matchers.any(AuthToken.class));

        verify(mailServiceMock, times(1)).sendEmailVerificationEmail(eq(SECURE_TOKEN_STRING), eq(USER_EMAIL));
        verifyNoMoreInteractions(serviceMock);
        verifyNoMoreInteractions(cryptoMock);
        verifyNoMoreInteractions(mailServiceMock);
    }

    @Test
    public void testAdminSignupResentConfirmationEmail() throws Exception {
        doThrow(new SgSignupForRegisteredButNonVerifiedEmailException(signupDto.getEmail())).when(serviceMock).signupAdmin(signupDto);
        when(serviceMock.getAccountPrincipal(signupDto.getEmail())).thenReturn(nonVerifiedAdminUserAccountPrincipalDto);
        when(cryptoMock.encryptSecurityToken(org.mockito.Matchers.any(AuthToken.class))).thenReturn(SECURE_TOKEN_STRING);

        mockMvc.perform(post(RequestPath.REQUEST_SIGNUP_ADMIN_USER)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jacksonObjectMapper.writeValueAsString(signupDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(CustomMediaTypes.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.status", is(SignupStatus.STATUS_CONFIRMATION_EMAIL_RESENT)));

        verify(serviceMock, times(1)).signupAdmin(signupDto);
        verify(serviceMock, times(1)).getAccountPrincipal(signupDto.getEmail());
        verify(cryptoMock, times(1)).encryptSecurityToken(org.mockito.Matchers.any(AuthToken.class));

        verify(mailServiceMock, times(1)).sendEmailVerificationEmail(eq(SECURE_TOKEN_STRING), eq(USER_EMAIL));
        verifyNoMoreInteractions(serviceMock);
        verifyNoMoreInteractions(cryptoMock);
        verifyNoMoreInteractions(mailServiceMock);
    }

    @Test
    public void testUserSignupAlreadyRegistered() throws Exception {
        doThrow(new SgSignupAlreadyCompletedException(USER_EMAIL)).when(serviceMock).signupUser(signupDto);

        mockMvc.perform(post(RequestPath.REQUEST_SIGNUP_USER)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jacksonObjectMapper.writeValueAsString(signupDto)))
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
        doThrow(new SgSignupAlreadyCompletedException(USER_EMAIL)).when(serviceMock).signupAdmin(signupDto);

        mockMvc.perform(post(RequestPath.REQUEST_SIGNUP_ADMIN_USER)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jacksonObjectMapper.writeValueAsString(signupDto)))
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
        when(serviceMock.getAccountPrincipal(signupDto.getEmail())).thenReturn(nonVerifiedUserAccountPrincipalDto);
        when(cryptoMock.encryptSecurityToken(org.mockito.Matchers.any(AuthToken.class))).thenReturn(SECURE_TOKEN_STRING);

        mockMvc.perform(post(RequestPath.REQUEST_SIGNUP_USER)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jacksonObjectMapper.writeValueAsString(signupDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(CustomMediaTypes.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.status", is(SignupStatus.STATUS_SUCCESS)))
                .andExpect(header().string(CustomHttpHeaders.X_ACCOUNT_ID, org.hamcrest.Matchers.any(String.class)));

        verify(serviceMock, times(1)).signupUser(signupDto);
        verify(serviceMock, times(1)).getAccountPrincipal(signupDto.getEmail());
        verify(cryptoMock, times(1)).encryptSecurityToken(org.mockito.Matchers.any(AuthToken.class));

        verify(mailServiceMock, times(1)).sendEmailVerificationEmail(eq(SECURE_TOKEN_STRING), eq(USER_EMAIL));
        verifyNoMoreInteractions(serviceMock);
        verifyNoMoreInteractions(cryptoMock);
        verifyNoMoreInteractions(mailServiceMock);
    }

    @Test
    public void testAdminSignupSuccessfully() throws Exception {
        when(serviceMock.getAccountPrincipal(signupDto.getEmail())).thenReturn(nonVerifiedAdminUserAccountPrincipalDto);
        when(cryptoMock.encryptSecurityToken(org.mockito.Matchers.any(AuthToken.class))).thenReturn(SECURE_TOKEN_STRING);

        mockMvc.perform(post(RequestPath.REQUEST_SIGNUP_ADMIN_USER)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jacksonObjectMapper.writeValueAsString(signupDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(CustomMediaTypes.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.status", is(SignupStatus.STATUS_SUCCESS)))
                .andExpect(header().string(CustomHttpHeaders.X_ACCOUNT_ID, org.hamcrest.Matchers.any(String.class)));

        verify(serviceMock, times(1)).signupAdmin(signupDto);
        verify(serviceMock, times(1)).getAccountPrincipal(signupDto.getEmail());
        verify(cryptoMock, times(1)).encryptSecurityToken(org.mockito.Matchers.any(AuthToken.class));

        verify(mailServiceMock, times(1)).sendEmailVerificationEmail(eq(SECURE_TOKEN_STRING), eq(USER_EMAIL));
        verifyNoMoreInteractions(serviceMock);
        verifyNoMoreInteractions(cryptoMock);
        verifyNoMoreInteractions(mailServiceMock);
    }

    @BeforeClass
    public static void setup() {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(ACCOUNT_ID, null));
    }

    @AfterClass
    public static void tearDown() {
        SecurityContextHolder.getContext().setAuthentication(null);
    }

    @Test
    public void testCompleteSignupAccountNotFound() throws Exception {
        doThrow(new SgAccountNotFoundException(ACCOUNT_ID)).when(serviceMock).completeSignup(ACCOUNT_ID, completeSignupDto);

        mockMvc.perform(post(RequestPath.REQUEST_COMPLETE_SIGNUP)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jacksonObjectMapper.writeValueAsString(completeSignupDto)))
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
        doThrow(new SgSignupAlreadyCompletedException(USER_EMAIL)).when(serviceMock).completeSignup(ACCOUNT_ID, completeSignupDto);

        mockMvc.perform(post(RequestPath.REQUEST_COMPLETE_SIGNUP)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jacksonObjectMapper.writeValueAsString(completeSignupDto)))
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
        mockMvc.perform(post(RequestPath.REQUEST_COMPLETE_SIGNUP)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jacksonObjectMapper.writeValueAsString(completeSignupDto)))
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
    public void testSigninAccountNotFound() throws Exception {
        doThrow(new SgAccountNotFoundException(signinDto.getEmail())).when(serviceMock).signIn(signinDto);

        mockMvc.perform(post(RequestPath.REQUEST_SIGNIN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jacksonObjectMapper.writeValueAsString(signinDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(CustomMediaTypes.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.status", is(SigninStatus.STATUS_USER_NOT_FOUND)));

        verify(serviceMock, times(1)).signIn(signinDto);

        verifyNoMoreInteractions(serviceMock);
        verifyNoMoreInteractions(cryptoMock);
        verifyNoMoreInteractions(mailServiceMock);
    }

    @Test
    public void testSigninInvalidPassword() throws Exception {
        doThrow(new SgInvalidPasswordException()).when(serviceMock).signIn(signinDto);

        mockMvc.perform(post(RequestPath.REQUEST_SIGNIN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jacksonObjectMapper.writeValueAsString(signinDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(CustomMediaTypes.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.status", is(SigninStatus.STATUS_WRONG_PASSWORD)));

        verify(serviceMock, times(1)).signIn(signinDto);

        verifyNoMoreInteractions(serviceMock);
        verifyNoMoreInteractions(cryptoMock);
        verifyNoMoreInteractions(mailServiceMock);
    }

    @Test
    public void testSigninEmailNonVerified() throws Exception {
        doThrow(new SgEmailNonVerifiedException(signinDto.getEmail())).when(serviceMock).signIn(signinDto);

        mockMvc.perform(post(RequestPath.REQUEST_SIGNIN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jacksonObjectMapper.writeValueAsString(signinDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(CustomMediaTypes.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.status", is(SigninStatus.STATUS_EMAIL_NOT_VERIFIED)));

        verify(serviceMock, times(1)).signIn(signinDto);

        verifyNoMoreInteractions(serviceMock);
        verifyNoMoreInteractions(cryptoMock);
        verifyNoMoreInteractions(mailServiceMock);
    }

    @Test
    public void testSigninSuccessfully() throws Exception {
        when(serviceMock.getAccountPrincipal(signinDto.getEmail())).thenReturn(verifiedUserAccountPrincipalDto);
        when(cryptoMock.encryptSecurityToken(org.mockito.Matchers.any(AuthToken.class))).thenReturn(SECURE_TOKEN_STRING);

        mockMvc.perform(post(RequestPath.REQUEST_SIGNIN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jacksonObjectMapper.writeValueAsString(signinDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(CustomMediaTypes.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.status", is(SigninStatus.STATUS_SUCCESS)))
                .andExpect(header().string(CustomHttpHeaders.X_AUTH_TOKEN, is(SECURE_TOKEN_STRING)));

        verify(serviceMock, times(1)).signIn(signinDto);
        verify(serviceMock, times(1)).getAccountPrincipal(signinDto.getEmail());
        verify(cryptoMock, times(1)).encryptSecurityToken(org.mockito.Matchers.any(AuthToken.class));

        verifyNoMoreInteractions(serviceMock);
        verifyNoMoreInteractions(cryptoMock);
        verifyNoMoreInteractions(mailServiceMock);
    }

}