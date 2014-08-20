/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.sg_rest_api.test;

import com.sg.constants.CompleteSignupStatus;
import com.sg.sg_rest_api.utils.CustomMediaTypes;
import com.sg.sg_rest_api.test.configuration.WebApplicationUnitTestContext;
import com.sg.domain.service.SgService;
import com.sg.sg_rest_api.configuration.ServletContext;
import com.sg.constants.RequestPath;
import com.sg.constants.Roles;
import com.sg.constants.SignupStatus;
import com.sg.constants.TokenExpirationType;
import com.sg.dto.SignupDto;
import com.sg.dto.AccountDto;
import com.sg.domain.service.SgMailService;
import com.sg.domain.service.AuthToken;
import com.sg.domain.service.SgCryptoService;
import com.sg.domain.service.exception.SgAccountNotFoundException;
import com.sg.domain.service.exception.SgSignupAlreadyCompletedException;
import com.sg.domain.service.exception.SgSignupForRegisteredButNonVerifiedEmailException;
import com.sg.dto.CompleteSignupDto;
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
    private static final CompleteSignupDto completeSignupDto;

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
    }

    @Test
    public void testUserSignupResentConfirmationEmail() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        doThrow(new SgSignupForRegisteredButNonVerifiedEmailException(signupDto.getEmail())).when(serviceMock).signupUser(signupDto);
        when(serviceMock.getAccountIdByRegistrationEmail(signupDto.getEmail())).thenReturn(nonVerifiedUserAccountDto.getId());
        when(serviceMock.getAccountInfo(nonVerifiedUserAccountDto.getId())).thenReturn(nonVerifiedUserAccountDto);
        when(cryptoMock.getTokenString(org.mockito.Matchers.any(AuthToken.class))).thenReturn(SECURE_TOKEN_STRING);

        mockMvc.perform(post(RequestPath.REQUEST_SIGNUP_USER)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(signupDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(CustomMediaTypes.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.status", is(SignupStatus.STATUS_CONFIRMATION_EMAIL_RESENT)));

        verify(serviceMock, times(1)).signupUser(signupDto);
        verify(serviceMock, times(1)).getAccountIdByRegistrationEmail(signupDto.getEmail());
        verify(serviceMock, times(1)).getAccountInfo(ACCOUNT_ID);
        verify(cryptoMock, times(1)).getTokenString(org.mockito.Matchers.any(AuthToken.class));

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
        when(cryptoMock.getTokenString(org.mockito.Matchers.any(AuthToken.class))).thenReturn(SECURE_TOKEN_STRING);

        mockMvc.perform(post(RequestPath.REQUEST_SIGNUP_ADMIN_USER)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(signupDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(CustomMediaTypes.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.status", is(SignupStatus.STATUS_CONFIRMATION_EMAIL_RESENT)));

        verify(serviceMock, times(1)).signupAdmin(signupDto);
        verify(serviceMock, times(1)).getAccountIdByRegistrationEmail(signupDto.getEmail());
        verify(serviceMock, times(1)).getAccountInfo(ACCOUNT_ID);
        verify(cryptoMock, times(1)).getTokenString(org.mockito.Matchers.any(AuthToken.class));

        verify(mailServiceMock, times(1)).sendEmailVerificationEmail(eq(SECURE_TOKEN_STRING), eq(USER_EMAIL));
        verifyNoMoreInteractions(serviceMock);
        verifyNoMoreInteractions(cryptoMock);
        verifyNoMoreInteractions(mailServiceMock);
    }
    
    @Test
    public void testUserSignupAlreadyRegistered() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        doThrow(new SgSignupAlreadyCompletedException()).when(serviceMock).signupUser(signupDto);

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

        doThrow(new SgSignupAlreadyCompletedException()).when(serviceMock).signupAdmin(signupDto);

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
        when(cryptoMock.getTokenString(org.mockito.Matchers.any(AuthToken.class))).thenReturn(SECURE_TOKEN_STRING);

        mockMvc.perform(post(RequestPath.REQUEST_SIGNUP_USER)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(signupDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(CustomMediaTypes.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.status", is(SignupStatus.STATUS_SUCCESS)));

        verify(serviceMock, times(1)).signupUser(signupDto);
        verify(serviceMock, times(1)).getAccountIdByRegistrationEmail(signupDto.getEmail());
        verify(serviceMock, times(1)).getAccountInfo(ACCOUNT_ID);
        verify(cryptoMock, times(1)).getTokenString(org.mockito.Matchers.any(AuthToken.class));

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
        when(cryptoMock.getTokenString(org.mockito.Matchers.any(AuthToken.class))).thenReturn(SECURE_TOKEN_STRING);

        mockMvc.perform(post(RequestPath.REQUEST_SIGNUP_ADMIN_USER)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(signupDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(CustomMediaTypes.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.status", is(SignupStatus.STATUS_SUCCESS)));

        verify(serviceMock, times(1)).signupAdmin(signupDto);
        verify(serviceMock, times(1)).getAccountIdByRegistrationEmail(signupDto.getEmail());
        verify(serviceMock, times(1)).getAccountInfo(ACCOUNT_ID);
        verify(cryptoMock, times(1)).getTokenString(org.mockito.Matchers.any(AuthToken.class));

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

        doThrow(new SgSignupAlreadyCompletedException()).when(serviceMock).completeSignup(ACCOUNT_ID, completeSignupDto);
        
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

    /*

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

    private static class TokenMatcher extends BaseMatcher<String> {

        private SgCryptoServiceImpl security;

        private Long userId;
        private List<String> authorities;

        private String reason;

        public TokenMatcher(SgCryptoServiceImpl security) {
            this.security = security;
        }

        public boolean matches(Object item) {
            if (item == null || !(item instanceof String)) {
                return false;
            }

            String encryptedToken = (String) item;
            AuthToken token;
            try {
                token = security.getTokenFromString(encryptedToken);
            } catch (SgCryptoException e) {
                reason = e.getMessage();
                return false;
            }

            if (!userId.equals(token.getAccountId())) {
                reason = "Non expected userId";
                return false;
            }
            if (!authorities.equals(token.getAuthorities())) {
                reason = "Non expected authorities";
                return false;
            }
            return true;
        }

        public void describeTo(Description description) {
            description.appendText(reason);
        }

        public List<String> getAuthorities() {
            return authorities;
        }

        public void setAuthorities(List<String> authorities) {
            this.authorities = authorities;
        }

        public Long getUserId() {
            return userId;
        }

        public void setUserId(Long userId) {
            this.userId = userId;
        }

    }
    */
}
