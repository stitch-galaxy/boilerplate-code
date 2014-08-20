package com.sg.domain.test.jpa;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import com.sg.constants.Roles;
import com.sg.dto.CanvasDto;
import com.sg.dto.CanvasRefDto;
import com.sg.dto.CanvasUpdateDto;
import com.sg.dto.ThreadDto;
import com.sg.dto.ThreadRefDto;
import com.sg.dto.ThreadUpdateDto;
import com.sg.domain.service.SgService;
import com.sg.domain.service.exception.SgAccountNotFoundException;
import com.sg.domain.service.exception.SgCanvasAlreadyExistsException;
import com.sg.domain.service.exception.SgCanvasNotFoundException;
import com.sg.domain.service.exception.SgEmailNonVerifiedException;
import com.sg.domain.service.exception.SgInvalidPasswordException;
import com.sg.domain.service.exception.SgSignupAlreadyCompletedException;
import com.sg.domain.service.exception.SgSignupForRegisteredButNonVerifiedEmailException;
import com.sg.domain.service.exception.SgThreadAlreadyExistsException;
import com.sg.domain.service.exception.SgThreadNotFoundException;
import com.sg.domain.spring.configuration.JpaContext;
import com.sg.domain.spring.configuration.JpaServiceContext;
import com.sg.domain.spring.configuration.MapperContext;
import com.sg.domain.spring.configuration.ValidatorContext;
import com.sg.dto.AccountDto;
import com.sg.dto.CompleteSignupDto;
import com.sg.dto.SigninDto;
import com.sg.dto.SignupDto;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import junit.framework.Assert;
import org.joda.time.LocalDate;
import org.junit.After;
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
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = {ValidatorContext.class, JpaContext.class, MapperContext.class, JpaServiceContext.class})
public class JpaServiceTest {

    @Autowired
    private SgService service;

    @Autowired
    private DataSource dataSource;

    public JpaServiceTest() {
    }

    private static final String AIDA_14 = "Aida 14";
    private static final String AIDA_18 = "Aida 18";

    private static final String DMC = "DMC";
    private static final String ANCHOR = "Anchor";

    private static final String USER_LAST_NAME = "Tarasova";
    private static final String USER_FIRST_NAME = "Nadezhda";
    private static final LocalDate USER_BIRTH_DATE = LocalDate.parse("1985-01-28");
    private static final String USER_EMAIL = "test@example.com";
    private static final String USER_PASSWORD = "secret";
    private static final String USER_INCORRECT_PASSWORD = "bad password";

    private static final SignupDto signupDto;
    
    private static final SigninDto signinDto;

    private static final CompleteSignupDto completeSignupDto;

    private static final ThreadDto dmcThreadDto;
    private static final ThreadDto anchorThreadDto;
    
    private static final CanvasDto aida14CanvasDto;
    private static final CanvasDto aida18CanvasDto;

    static {
        dmcThreadDto = new ThreadDto();
        dmcThreadDto.setCode(DMC);

        anchorThreadDto = new ThreadDto();
        anchorThreadDto.setCode(ANCHOR);
        
        aida14CanvasDto = new CanvasDto();
        aida14CanvasDto.setCode(AIDA_14);
        aida14CanvasDto.setStitchesPerInch(new BigDecimal(14));
        
        aida18CanvasDto = new CanvasDto();
        aida18CanvasDto.setCode(AIDA_18);
        aida18CanvasDto.setStitchesPerInch(new BigDecimal(18));
        

        signupDto = new SignupDto();
        signupDto.setEmail(USER_EMAIL);
        signupDto.setUserBirthDate(USER_BIRTH_DATE);
        signupDto.setUserFirstName(USER_FIRST_NAME);
        signupDto.setUserLastName(USER_LAST_NAME);
        
        signinDto = new SigninDto();
        signinDto.setPassword(USER_PASSWORD);
        signinDto.setEmail(USER_EMAIL);

        completeSignupDto = new CompleteSignupDto();
        completeSignupDto.setPassword(USER_PASSWORD);
    }
    
    @Test
    public void testCanvasCreate()
    {
        service.create(aida14CanvasDto);
        try {
            service.create(aida14CanvasDto);
            Assert.fail("Expected " + SgCanvasAlreadyExistsException.class.getName());
        } catch (SgCanvasAlreadyExistsException e) {
        }
    }
    
    @Test
    public void testCanvasesList() {
        service.create(aida14CanvasDto);
        service.create(aida18CanvasDto);
        List<CanvasDto> list = new ArrayList<CanvasDto>();
        list.add(aida14CanvasDto);
        list.add(aida18CanvasDto);
        
        List<CanvasDto> result = service.listCanvases();
        Assert.assertEquals(list, result);
    }
    
    @Test
    public void testCanvasUpdate() {
        service.create(aida14CanvasDto);

        CanvasRefDto ref = new CanvasRefDto();
        ref.setCode(AIDA_18);
        CanvasUpdateDto updateDto = new CanvasUpdateDto();
        updateDto.setRef(ref);
        updateDto.setDto(aida14CanvasDto);

        try {
            service.update(updateDto);
            Assert.fail("Expected " + SgCanvasNotFoundException.class.getName());
        } catch (SgCanvasNotFoundException e) {
        }

        ref = new CanvasRefDto();
        ref.setCode(AIDA_14);
        updateDto = new CanvasUpdateDto();
        updateDto.setRef(ref);
        updateDto.setDto(aida18CanvasDto);
        service.update(updateDto);
        List<CanvasDto> list = new ArrayList<CanvasDto>();
        list.add(aida18CanvasDto);
        Assert.assertEquals(list, service.listCanvases());
        service.create(aida14CanvasDto);

        try {
            service.update(updateDto);
            Assert.fail("Expected " + SgCanvasAlreadyExistsException.class.getName());
        } catch (SgCanvasAlreadyExistsException e) {
        }
    }
    
    @Test
    public void testCanvasDelete() {
        service.create(aida14CanvasDto);

        CanvasRefDto ref = new CanvasRefDto();
        ref.setCode(AIDA_18);

        try {
            service.delete(ref);
            Assert.fail("Expected " + SgCanvasNotFoundException.class.getName());
        } catch (SgCanvasNotFoundException e) {
        }
        
        ref = new CanvasRefDto();
        ref.setCode(AIDA_14);
        
        service.delete(ref);
        
        Assert.assertEquals(0, service.listCanvases().size());
    }

    @Test
    public void testThreadCreate() {
        service.create(dmcThreadDto);
        try {
            service.create(dmcThreadDto);
            Assert.fail("Expected " + SgThreadAlreadyExistsException.class.getName());
        } catch (SgThreadAlreadyExistsException e) {
        }
    }

    @Test
    public void testThreadsList() {
        service.create(dmcThreadDto);
        service.create(anchorThreadDto);
        List<ThreadDto> list = new ArrayList<ThreadDto>();
        list.add(dmcThreadDto);
        list.add(anchorThreadDto);
        Assert.assertEquals(list, service.listThreads());
    }

    @Test
    public void testThreadUpdate() {
        service.create(dmcThreadDto);

        ThreadRefDto ref = new ThreadRefDto();
        ref.setCode(ANCHOR);
        ThreadUpdateDto updateDto = new ThreadUpdateDto();
        updateDto.setRef(ref);
        updateDto.setDto(dmcThreadDto);

        try {
            service.update(updateDto);
            Assert.fail("Expected " + SgThreadNotFoundException.class.getName());
        } catch (SgThreadNotFoundException e) {
        }

        ref = new ThreadRefDto();
        ref.setCode(DMC);
        updateDto = new ThreadUpdateDto();
        updateDto.setRef(ref);
        updateDto.setDto(anchorThreadDto);
        service.update(updateDto);
        List<ThreadDto> list = new ArrayList<ThreadDto>();
        list.add(anchorThreadDto);
        Assert.assertEquals(list, service.listThreads());
        service.create(dmcThreadDto);

        try {
            service.update(updateDto);
            Assert.fail("Expected " + SgThreadAlreadyExistsException.class.getName());
        } catch (SgThreadAlreadyExistsException e) {
        }
    }

    @Test
    public void testThreadDelete() {
        service.create(dmcThreadDto);

        ThreadRefDto ref = new ThreadRefDto();
        ref.setCode(ANCHOR);

        try {
            service.delete(ref);
            Assert.fail("Expected " + SgThreadNotFoundException.class.getName());
        } catch (SgThreadNotFoundException e) {
        }
        
        ref = new ThreadRefDto();
        ref.setCode(DMC);
        
        service.delete(ref);
        
        Assert.assertEquals(0, service.listThreads().size());
    }
    
    @Test 
    public void testGetAccountIdByRegistrationEmailThrowsExceptionIfAccountNotFound()
    {
        try
        {
            service.getAccountIdByRegistrationEmail(signupDto.getEmail());
            Assert.fail("Expected  " + SgAccountNotFoundException.class.getName());
        }
        catch(SgAccountNotFoundException e){
        }
    }
    
    @Test 
    public void testGetAccountInfoThrowsExceptionIfAccountNotFound()
    {
        try
        {
            service.getAccountInfo(1L);
            Assert.fail("Expected  " + SgAccountNotFoundException.class.getName());
        }
        catch(SgAccountNotFoundException e){
        }
    }
    
    @Test
    public void testSignupAdmin() {
        service.signupAdmin(signupDto);
        
        Long accountId = service.getAccountIdByRegistrationEmail(signupDto.getEmail());
        
        AccountDto accountDto = service.getAccountInfo(accountId);
        Assert.assertEquals(signupDto.getEmail(), accountDto.getEmail());
        Assert.assertEquals(signupDto.getUserBirthDate(), accountDto.getUserBirthDate());
        Assert.assertEquals(signupDto.getUserFirstName(), accountDto.getUserFirstName());
        Assert.assertEquals(signupDto.getUserLastName(), accountDto.getUserLastName());
        Assert.assertEquals(Boolean.FALSE, accountDto.getEmailVerified());
        
        Assert.assertTrue(accountDto.getRoles().size() == 2);
        Assert.assertTrue(accountDto.getRoles().contains(Roles.ROLE_USER));
        Assert.assertTrue(accountDto.getRoles().contains(Roles.ROLE_ADMIN));
        
        try{
            service.signupUser(signupDto);
            Assert.fail("Expected " + SgSignupForRegisteredButNonVerifiedEmailException.class.getName());
        }
        catch(SgSignupForRegisteredButNonVerifiedEmailException e){
        }
        
        service.completeSignup(accountId, completeSignupDto);
        
        try{
            service.signupUser(signupDto);
            Assert.fail("Expected " + SgSignupAlreadyCompletedException.class.getName());
        }
        catch(SgSignupAlreadyCompletedException e){
        }
    }
    
    @Test
    public void testSignupUser() {
        service.signupUser(signupDto);
        
        Long accountId = service.getAccountIdByRegistrationEmail(signupDto.getEmail());
        
        AccountDto accountDto = service.getAccountInfo(accountId);
        Assert.assertEquals(signupDto.getEmail(), accountDto.getEmail());
        Assert.assertEquals(signupDto.getUserBirthDate(), accountDto.getUserBirthDate());
        Assert.assertEquals(signupDto.getUserFirstName(), accountDto.getUserFirstName());
        Assert.assertEquals(signupDto.getUserLastName(), accountDto.getUserLastName());
        Assert.assertEquals(Boolean.FALSE, accountDto.getEmailVerified());
        
        Assert.assertTrue(accountDto.getRoles().size() == 1);
        Assert.assertTrue(accountDto.getRoles().contains(Roles.ROLE_USER));
        
        try{
            service.signupUser(signupDto);
            Assert.fail("Expected " + SgSignupForRegisteredButNonVerifiedEmailException.class.getName());
        }
        catch(SgSignupForRegisteredButNonVerifiedEmailException e){
        }
        
        service.completeSignup(accountId, completeSignupDto);
        
        try{
            service.signupUser(signupDto);
            Assert.fail("Expected " + SgSignupAlreadyCompletedException.class.getName());
        }
        catch(SgSignupAlreadyCompletedException e){
        }
    }
    
    @Test
    public void testCompleteSignup() 
    {
        try{
            service.completeSignup(1L, completeSignupDto);
            Assert.fail("Expected " + SgAccountNotFoundException.class.getName());
        }
        catch(SgAccountNotFoundException e) {
        }
        
        service.signupUser(signupDto);
        
        Long accountId = service.getAccountIdByRegistrationEmail(signupDto.getEmail());
        
        service.completeSignup(accountId, completeSignupDto);
        
        try
        {
            service.completeSignup(accountId, completeSignupDto);
            Assert.fail("Expected " + SgSignupAlreadyCompletedException.class.getName());
        }
        catch(SgSignupAlreadyCompletedException e) {
        }
    }
    
    @Test
    public void testSignin()
    {
        try{
            service.signIn(signinDto);
            Assert.fail("Expected " + SgAccountNotFoundException.class.getName());
        }
        catch(SgAccountNotFoundException e)
        {}
        
        service.signupUser(signupDto);
        
        try{
            service.signIn(signinDto);
            Assert.fail("Expected " + SgEmailNonVerifiedException.class.getName());
        }
        catch(SgEmailNonVerifiedException e)
        {}
        
        Long accountId = service.getAccountIdByRegistrationEmail(signupDto.getEmail());
        service.completeSignup(accountId, completeSignupDto);
        
        service.signIn(signinDto);
        
        SigninDto invalidSignInDto = new SigninDto();
        invalidSignInDto.setEmail(signinDto.getEmail());
        invalidSignInDto.setPassword(USER_INCORRECT_PASSWORD);
        
        try{
            service.signIn(invalidSignInDto);
            Assert.fail("Expected " + SgInvalidPasswordException.class.getName());
        }
        catch(SgInvalidPasswordException e)
        {}
    }

    @After
    public void cleanupData() throws Exception {
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            try {
                Statement stmt = connection.createStatement();
                try {
                    stmt.execute("TRUNCATE SCHEMA PUBLIC RESTART IDENTITY AND COMMIT NO CHECK");
                    connection.commit();
                } finally {
                    stmt.close();
                }
            } catch (SQLException e) {
                connection.rollback();
                throw new Exception(e);
            }
        } catch (SQLException e) {
            throw new Exception(e);
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }
}
