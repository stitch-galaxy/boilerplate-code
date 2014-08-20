package com.sg.domain.test.jpa;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import com.sg.constants.Roles;
import com.sg.constants.TokenExpirationIntervals;
import com.sg.constants.TokenExpirationType;
import com.sg.domain.service.AuthToken;
import com.sg.domain.service.SgCryptoService;
import com.sg.domain.service.exception.SgCryptoException;
import com.sg.domain.service.exception.SgInvalidTokenException;
import com.sg.domain.service.exception.SgTokenExpiredException;
import com.sg.domain.spring.configuration.SgCryptoContext;
import com.sg.dto.AccountDto;
import java.util.Arrays;
import junit.framework.Assert;
import org.joda.time.LocalDate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

/**
 *
 * @author tarasev
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = {SgCryptoContext.class})
public class SgCryptoServiceTest {

    private static final Long ACCOUNT_ID = 1L;
    private static final String USER_LAST_NAME = "Tarasova";
    private static final String USER_FIRST_NAME = "Nadezhda";
    private static final LocalDate USER_BIRTH_DATE = LocalDate.parse("1985-01-28");
    private static final String USER_EMAIL = "test@example.com";
    private static final AccountDto accountDto;

    static {
        accountDto = new AccountDto();
        accountDto.setId(ACCOUNT_ID);
        accountDto.setRoles(Arrays.asList(Roles.ROLE_ADMIN, Roles.ROLE_USER));
        accountDto.setEmail(USER_EMAIL);
        accountDto.setEmailVerified(Boolean.TRUE);
        accountDto.setUserBirthDate(USER_BIRTH_DATE);
        accountDto.setUserFirstName(USER_FIRST_NAME);
        accountDto.setUserFirstName(USER_LAST_NAME);
    }

    @Autowired
    private SgCryptoService cryptoService;

    @Test
    public void testGoodLongToken() throws SgCryptoException {
        AuthToken authToken = new AuthToken(accountDto, TokenExpirationType.LONG_TOKEN);

        String token = cryptoService.getTokenString(authToken);

        AuthToken decryptedAuthToken = cryptoService.getTokenFromString(token);

        Assert.assertEquals(authToken, decryptedAuthToken);
    }

    @Test
    public void testExpiredLongToken() throws SgCryptoException {
        AuthToken authToken = new AuthToken(accountDto, TokenExpirationType.LONG_TOKEN);
        authToken.setExpirationMillis(authToken.getExpirationMillis() - TokenExpirationIntervals.LONG_TOKEN_EXPIRATION_INTERVAL);

        String token = cryptoService.getTokenString(authToken);

        try
        {
            cryptoService.getTokenFromString(token);
            Assert.fail("Expected " + SgTokenExpiredException.class.getName());
        }
        catch(SgTokenExpiredException e){
        }
    }
    
    @Test
    public void testGoodSessionToken() throws SgCryptoException {
        AuthToken authToken = new AuthToken(accountDto, TokenExpirationType.USER_SESSION_TOKEN);

        String token = cryptoService.getTokenString(authToken);

        AuthToken decryptedAuthToken = cryptoService.getTokenFromString(token);

        Assert.assertEquals(authToken, decryptedAuthToken);
    }

    @Test
    public void testExpiredSessionToken() throws SgCryptoException {
        AuthToken authToken = new AuthToken(accountDto, TokenExpirationType.USER_SESSION_TOKEN);
        authToken.setExpirationMillis(authToken.getExpirationMillis() - TokenExpirationIntervals.USER_SESSION_TOKEN_EXPIRATION_INTERVAL);

        String token = cryptoService.getTokenString(authToken);

        try
        {
            cryptoService.getTokenFromString(token);
            Assert.fail("Expected " + SgTokenExpiredException.class.getName());
        }
        catch(SgTokenExpiredException e){
        }
    }
    
    @Test
    public void testNeverExpiresToken() throws SgCryptoException {
        AuthToken authToken = new AuthToken(accountDto, TokenExpirationType.USER_SESSION_TOKEN);

        String token = cryptoService.getTokenString(authToken);

        AuthToken decryptedAuthToken = cryptoService.getTokenFromString(token);

        Assert.assertEquals(authToken, decryptedAuthToken);
    }
    
    @Test
    public void testBadToken() throws SgCryptoException
    {
        try
        {
            cryptoService.getTokenFromString(INVALID_TOKEN);
            Assert.fail("Expected " + SgInvalidTokenException.class.getName());
        }
        catch(SgInvalidTokenException e){
        }
    }
    private static final String INVALID_TOKEN = "INVALID_TOKEN";
}
