package com.sg.domain.test.jpa;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import com.sg.constants.Roles;
import com.sg.constants.Sex;
import com.sg.dto.request.CanvasCreateDto;
import com.sg.dto.request.CanvasDeleteDto;
import com.sg.dto.request.CanvasUpdateDto;
import com.sg.dto.request.ThreadCreateDto;
import com.sg.dto.request.ThreadDeleteDto;
import com.sg.dto.request.ThreadUpdateDto;
import com.sg.domain.service.SgService;
import com.sg.domain.service.exception.SgAccountNotFoundException;
import com.sg.domain.service.exception.SgCanvasAlreadyExistsException;
import com.sg.domain.service.exception.SgCanvasNotFoundException;
import com.sg.domain.service.exception.SgDataValidationException;
import com.sg.domain.service.exception.SgEmailNonVerifiedException;
import com.sg.domain.service.exception.SgInstallationAlreadyCompletedException;
import com.sg.domain.service.exception.SgInvalidPasswordException;
import com.sg.domain.service.exception.SgSignupAlreadyCompletedException;
import com.sg.domain.service.exception.SgSignupForRegisteredButNonVerifiedEmailException;
import com.sg.domain.service.exception.SgThreadAlreadyExistsException;
import com.sg.domain.service.exception.SgThreadNotFoundException;
import com.sg.domain.spring.configuration.JpaContext;
import com.sg.domain.spring.configuration.JpaServiceContext;
import com.sg.domain.spring.configuration.MapperContext;
import com.sg.domain.spring.configuration.ValidatorContext;
import com.sg.domain.test.spring.configuration.TestJpaServicePropertiesContextConfiguration;
import com.sg.dto.response.AccountPrincipalDto;
import com.sg.dto.request.CompleteSignupDto;
import com.sg.dto.request.ResetPasswordDto;
import com.sg.dto.request.SigninDto;
import com.sg.dto.request.SignupDto;
import com.sg.dto.request.UserInfoUpdateDto;
import com.sg.dto.response.CanvasesListDto;
import com.sg.dto.response.ThreadsListDto;
import com.sg.dto.response.UserInfoDto;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import org.junit.Assert;
import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

/**
 *
 * @author tarasev
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = {TestJpaServicePropertiesContextConfiguration.class, ValidatorContext.class, JpaContext.class, MapperContext.class, JpaServiceContext.class})
public class JpaServiceTest {

    @Value("${admin.email}")
    private String adminEmail;
    @Value("${admin.password}")
    private String adminPassword;
    @Value("${user.email}")
    private String userEmail;
    @Value("${user.password}")
    private String userPassword;

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
    private static final String NEW_USER_LAST_NAME = "Тарасов";
    private static final String NEW_USER_FIRST_NAME = "Евгений";
    private static final LocalDate USER_BIRTH_DATE = LocalDate.parse("1985-01-28");
    private static final String USER_EMAIL = "test@example.com";
    private static final String USER_PASSWORD = "1GoodPassword!";
    private static final String NEW_USER_PASSWORD = "1NewPassword!";
    private static final String USER_INCORRECT_PASSWORD = "1InvalidPassword!";

    private static final SignupDto signupDto;

    private static final SigninDto signinDto;

    private static final CompleteSignupDto completeSignupDto;

    private static final ThreadCreateDto dmcThreadDto;
    private static final ThreadCreateDto anchorThreadDto;

    private static final ThreadsListDto.ThreadInfo dmcThreadInfoDto;
    private static final ThreadsListDto.ThreadInfo anchorThreadInfoDto;

    private static final CanvasCreateDto aida14CanvasDto;
    private static final CanvasCreateDto aida18CanvasDto;

    private static final CanvasesListDto.CanvasInfo aida14CanvasInfoDto;
    private static final CanvasesListDto.CanvasInfo aida18CanvasInfoDto;
    private static final BigDecimal STITCHES_14 = new BigDecimal(14);
    private static final BigDecimal STITCHES_18 = new BigDecimal(18);

    private static final UserInfoUpdateDto userInfoUpdateDto;
    private static final UserInfoDto userInfoDto;
    private static final String USER_NICK_NAME = "Жека Пират";

    private static final ResetPasswordDto resetPasswordDto;

    static {
        dmcThreadDto = new ThreadCreateDto();
        dmcThreadDto.setCode(DMC);

        dmcThreadInfoDto = new ThreadsListDto.ThreadInfo();
        dmcThreadInfoDto.setCode(DMC);

        anchorThreadDto = new ThreadCreateDto();
        anchorThreadDto.setCode(ANCHOR);

        anchorThreadInfoDto = new ThreadsListDto.ThreadInfo();
        anchorThreadInfoDto.setCode(ANCHOR);

        aida14CanvasDto = new CanvasCreateDto();
        aida14CanvasDto.setCode(AIDA_14);
        aida14CanvasDto.setStitchesPerInch(STITCHES_14);

        aida18CanvasDto = new CanvasCreateDto();
        aida18CanvasDto.setCode(AIDA_18);
        aida18CanvasDto.setStitchesPerInch(STITCHES_18);

        aida14CanvasInfoDto = new CanvasesListDto.CanvasInfo();
        aida14CanvasInfoDto.setCode(AIDA_14);
        aida14CanvasInfoDto.setStitchesPerInch(STITCHES_14);

        aida18CanvasInfoDto = new CanvasesListDto.CanvasInfo();
        aida18CanvasInfoDto.setCode(AIDA_18);
        aida18CanvasInfoDto.setStitchesPerInch(STITCHES_18);

        signupDto = new SignupDto();
        signupDto.setEmail(USER_EMAIL);
        signupDto.setUserFirstName(USER_FIRST_NAME);
        signupDto.setUserLastName(USER_LAST_NAME);

        signinDto = new SigninDto();
        signinDto.setPassword(USER_PASSWORD);
        signinDto.setEmail(USER_EMAIL);

        completeSignupDto = new CompleteSignupDto();
        completeSignupDto.setPassword(USER_PASSWORD);

        userInfoUpdateDto = new UserInfoUpdateDto();
        userInfoUpdateDto.setNickname(USER_NICK_NAME);
        userInfoUpdateDto.setSex(Sex.MALE);
        userInfoUpdateDto.setUserBirthDate(USER_BIRTH_DATE);
        userInfoUpdateDto.setUserFirstName(NEW_USER_FIRST_NAME);
        userInfoUpdateDto.setUserLastName(NEW_USER_LAST_NAME);

        userInfoDto = new UserInfoDto();
        userInfoDto.setNickname(USER_NICK_NAME);
        userInfoDto.setSex(Sex.MALE);
        userInfoDto.setUserBirthDate(USER_BIRTH_DATE);
        userInfoDto.setUserFirstName(NEW_USER_FIRST_NAME);
        userInfoDto.setUserLastName(NEW_USER_LAST_NAME);

        resetPasswordDto = new ResetPasswordDto();
        resetPasswordDto.setPassword(NEW_USER_PASSWORD);
    }

    @Test
    public void testCanvasCreate() throws SgDataValidationException {
        service.create(aida14CanvasDto);
        try {
            service.create(aida14CanvasDto);
            Assert.fail("Expected " + SgCanvasAlreadyExistsException.class.getName());
        } catch (SgCanvasAlreadyExistsException e) {
        }
    }

    @Test
    public void testCanvasesList() throws SgDataValidationException {
        service.create(aida14CanvasDto);
        service.create(aida18CanvasDto);

        CanvasesListDto canvasesList = new CanvasesListDto();
        List<CanvasesListDto.CanvasInfo> list = new ArrayList<CanvasesListDto.CanvasInfo>();
        list.add(aida14CanvasInfoDto);
        list.add(aida18CanvasInfoDto);
        canvasesList.setCanvases(list);

        Assert.assertEquals(canvasesList, service.listCanvases());
    }

    @Test
    public void testCanvasUpdate() throws SgDataValidationException {
        service.create(aida14CanvasDto);

        CanvasUpdateDto updateDto = new CanvasUpdateDto();
        updateDto.setRefCode(AIDA_18);
        updateDto.setCode(AIDA_14);
        updateDto.setStitchesPerInch(STITCHES_14);

        try {
            service.update(updateDto);
            Assert.fail("Expected " + SgCanvasNotFoundException.class.getName());
        } catch (SgCanvasNotFoundException e) {
        }

        updateDto = new CanvasUpdateDto();
        updateDto.setRefCode(AIDA_14);
        updateDto.setCode(AIDA_18);
        updateDto.setStitchesPerInch(STITCHES_18);

        service.update(updateDto);

        CanvasesListDto canvasesList = new CanvasesListDto();
        List<CanvasesListDto.CanvasInfo> list = new ArrayList<CanvasesListDto.CanvasInfo>();
        list.add(aida18CanvasInfoDto);
        canvasesList.setCanvases(list);

        Assert.assertEquals(canvasesList, service.listCanvases());
        service.create(aida14CanvasDto);

        try {
            service.update(updateDto);
            Assert.fail("Expected " + SgCanvasAlreadyExistsException.class.getName());
        } catch (SgCanvasAlreadyExistsException e) {
        }
    }

    @Test
    public void testCanvasDelete() throws SgDataValidationException {
        service.create(aida14CanvasDto);

        CanvasDeleteDto ref = new CanvasDeleteDto();
        ref.setCode(AIDA_18);

        try {
            service.delete(ref);
            Assert.fail("Expected " + SgCanvasNotFoundException.class.getName());
        } catch (SgCanvasNotFoundException e) {
        }

        ref = new CanvasDeleteDto();
        ref.setCode(AIDA_14);

        service.delete(ref);

        Assert.assertEquals(0, service.listCanvases().getCanvases().size());
    }

    @Test
    public void testThreadCreate() throws SgDataValidationException {
        service.create(dmcThreadDto);
        try {
            service.create(dmcThreadDto);
            Assert.fail("Expected " + SgThreadAlreadyExistsException.class.getName());
        } catch (SgThreadAlreadyExistsException e) {
        }
    }

    @Test
    public void testThreadsList() throws SgDataValidationException {
        service.create(dmcThreadDto);
        service.create(anchorThreadDto);

        ThreadsListDto threadsList = new ThreadsListDto();
        List<ThreadsListDto.ThreadInfo> list = new ArrayList<ThreadsListDto.ThreadInfo>();
        list.add(dmcThreadInfoDto);
        list.add(anchorThreadInfoDto);
        threadsList.setThreads(list);
        Assert.assertEquals(threadsList, service.listThreads());
    }

    @Test
    public void testThreadUpdate() throws SgDataValidationException {
        service.create(dmcThreadDto);

        ThreadUpdateDto updateDto = new ThreadUpdateDto();
        updateDto.setRefCode(ANCHOR);
        updateDto.setCode(DMC);

        try {
            service.update(updateDto);
            Assert.fail("Expected " + SgThreadNotFoundException.class.getName());
        } catch (SgThreadNotFoundException e) {
        }

        updateDto = new ThreadUpdateDto();
        updateDto.setRefCode(DMC);
        updateDto.setCode(ANCHOR);
        service.update(updateDto);
        ThreadsListDto threadsList = new ThreadsListDto();
        List<ThreadsListDto.ThreadInfo> list = new ArrayList<ThreadsListDto.ThreadInfo>();
        list.add(anchorThreadInfoDto);
        threadsList.setThreads(list);

        Assert.assertEquals(threadsList, service.listThreads());

        service.create(dmcThreadDto);

        try {
            service.update(updateDto);
            Assert.fail("Expected " + SgThreadAlreadyExistsException.class.getName());
        } catch (SgThreadAlreadyExistsException e) {
        }
    }

    @Test
    public void testThreadDelete() throws SgDataValidationException {
        service.create(dmcThreadDto);

        ThreadDeleteDto ref = new ThreadDeleteDto();
        ref.setCode(ANCHOR);

        try {
            service.delete(ref);
            Assert.fail("Expected " + SgThreadNotFoundException.class.getName());
        } catch (SgThreadNotFoundException e) {
        }

        ref = new ThreadDeleteDto();
        ref.setCode(DMC);

        service.delete(ref);

        Assert.assertEquals(0, service.listThreads().getThreads().size());
    }

    @Test
    public void testGetAccountPrincipalThrowsExceptionIfAccountNotFound() throws SgDataValidationException {
        try {
            service.getAccountPrincipal(signupDto.getEmail());
            Assert.fail("Expected  " + SgAccountNotFoundException.class.getName());
        } catch (SgAccountNotFoundException e) {
        }
    }

    @Test
    public void testInstallation() throws SgDataValidationException {
        service.install();

        AccountPrincipalDto accountPrincipalDto = service.getAccountPrincipal(adminEmail);
        Assert.assertEquals(Boolean.TRUE, accountPrincipalDto.getEmailVerified());

        Assert.assertTrue(accountPrincipalDto.getRoles().size() == 2);
        Assert.assertTrue(accountPrincipalDto.getRoles().contains(Roles.ROLE_USER));
        Assert.assertTrue(accountPrincipalDto.getRoles().contains(Roles.ROLE_ADMIN));

        accountPrincipalDto = service.getAccountPrincipal(userEmail);
        Assert.assertEquals(Boolean.TRUE, accountPrincipalDto.getEmailVerified());

        Assert.assertTrue(accountPrincipalDto.getRoles().size() == 1);
        Assert.assertTrue(accountPrincipalDto.getRoles().contains(Roles.ROLE_USER));

        try {
            service.install();
            Assert.fail("Expected " + SgSignupForRegisteredButNonVerifiedEmailException.class.getName());
        } catch (SgInstallationAlreadyCompletedException e) {
        }
    }

    @Test
    public void testSignupAdmin() throws SgDataValidationException {
        service.signupAdmin(signupDto);

        AccountPrincipalDto accountPrincipalDto = service.getAccountPrincipal(signupDto.getEmail());
        Assert.assertEquals(Boolean.FALSE, accountPrincipalDto.getEmailVerified());

        Assert.assertTrue(accountPrincipalDto.getRoles().size() == 2);
        Assert.assertTrue(accountPrincipalDto.getRoles().contains(Roles.ROLE_USER));
        Assert.assertTrue(accountPrincipalDto.getRoles().contains(Roles.ROLE_ADMIN));

        try {
            service.signupUser(signupDto);
            Assert.fail("Expected " + SgSignupForRegisteredButNonVerifiedEmailException.class.getName());
        } catch (SgSignupForRegisteredButNonVerifiedEmailException e) {
        }

        service.completeSignup(accountPrincipalDto.getId(), completeSignupDto);

        try {
            service.signupUser(signupDto);
            Assert.fail("Expected " + SgSignupAlreadyCompletedException.class.getName());
        } catch (SgSignupAlreadyCompletedException e) {
        }
    }

    @Test
    public void testSignupUser() throws SgDataValidationException {
        service.signupUser(signupDto);

        AccountPrincipalDto accountPrincipalDto = service.getAccountPrincipal(signupDto.getEmail());
        Assert.assertEquals(Boolean.FALSE, accountPrincipalDto.getEmailVerified());

        Assert.assertTrue(accountPrincipalDto.getRoles().size() == 1);
        Assert.assertTrue(accountPrincipalDto.getRoles().contains(Roles.ROLE_USER));

        try {
            service.signupUser(signupDto);
            Assert.fail("Expected " + SgSignupForRegisteredButNonVerifiedEmailException.class.getName());
        } catch (SgSignupForRegisteredButNonVerifiedEmailException e) {
        }

        service.completeSignup(accountPrincipalDto.getId(), completeSignupDto);

        try {
            service.signupUser(signupDto);
            Assert.fail("Expected " + SgSignupAlreadyCompletedException.class.getName());
        } catch (SgSignupAlreadyCompletedException e) {
        }
    }

    @Test
    public void testCompleteSignup() throws SgDataValidationException {
        try {
            service.completeSignup(1L, completeSignupDto);
            Assert.fail("Expected " + SgAccountNotFoundException.class.getName());
        } catch (SgAccountNotFoundException e) {
        }

        service.signupUser(signupDto);

        AccountPrincipalDto dto = service.getAccountPrincipal(signupDto.getEmail());

        service.completeSignup(dto.getId(), completeSignupDto);

        try {
            service.completeSignup(dto.getId(), completeSignupDto);
            Assert.fail("Expected " + SgSignupAlreadyCompletedException.class.getName());
        } catch (SgSignupAlreadyCompletedException e) {
        }
    }

    @Test
    public void testSignin() throws SgDataValidationException {
        try {
            service.signIn(signinDto);
            Assert.fail("Expected " + SgAccountNotFoundException.class.getName());
        } catch (SgAccountNotFoundException e) {
        }

        service.signupUser(signupDto);

        try {
            service.signIn(signinDto);
            Assert.fail("Expected " + SgEmailNonVerifiedException.class.getName());
        } catch (SgEmailNonVerifiedException e) {
        }

        AccountPrincipalDto dto = service.getAccountPrincipal(signupDto.getEmail());

        service.completeSignup(dto.getId(), completeSignupDto);

        service.signIn(signinDto);

        SigninDto invalidSignInDto = new SigninDto();
        invalidSignInDto.setEmail(signinDto.getEmail());
        invalidSignInDto.setPassword(USER_INCORRECT_PASSWORD);

        try {
            service.signIn(invalidSignInDto);
            Assert.fail("Expected " + SgInvalidPasswordException.class.getName());
        } catch (SgInvalidPasswordException e) {
        }
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

    @Test
    public void testSetGetUserInformation() throws SgDataValidationException {
        service.signupUser(signupDto);
        AccountPrincipalDto dto = service.getAccountPrincipal(signupDto.getEmail());
        service.completeSignup(dto.getId(), completeSignupDto);

        try {
            service.getUserInfo(Long.MAX_VALUE);
            Assert.fail("Expected " + SgAccountNotFoundException.class.getName());
        } catch (SgAccountNotFoundException e) {
        }

        UserInfoDto userInfo = service.getUserInfo(dto.getId());
        Assert.assertEquals(USER_FIRST_NAME, userInfo.getUserFirstName());
        Assert.assertEquals(USER_LAST_NAME, userInfo.getUserLastName());
        Assert.assertEquals(null, userInfo.getUserBirthDate());
        Assert.assertEquals(null, userInfo.getNickname());

        service.setUserInfo(dto.getId(), userInfoUpdateDto);
        userInfo = service.getUserInfo(dto.getId());
        Assert.assertEquals(userInfoDto, userInfo);

    }

    @Test
    public void testResetPassword() throws SgDataValidationException {
        //TODO: when signin with FB will be completed cover with test: SgAccountWithoutEmailException

        service.signupUser(signupDto);
        AccountPrincipalDto dto = service.getAccountPrincipal(signupDto.getEmail());
        try {
            service.resetPassword(dto.getId(), resetPasswordDto);
            Assert.fail("Expected " + SgEmailNonVerifiedException.class.getName());
        } catch (SgEmailNonVerifiedException e) {
        }

        service.completeSignup(dto.getId(), completeSignupDto);

        try {
            service.resetPassword(Long.MIN_VALUE, resetPasswordDto);
            Assert.fail("Expected " + SgAccountNotFoundException.class.getName());
        } catch (SgAccountNotFoundException e) {
        }

        service.resetPassword(dto.getId(), resetPasswordDto);
        SigninDto signInWithNewPassword = new SigninDto();
        signInWithNewPassword.setPassword(NEW_USER_PASSWORD);
        signInWithNewPassword.setEmail(USER_EMAIL);
        service.signIn(signInWithNewPassword);
    }

    @Test
    public void testDeleteAccount() throws SgDataValidationException {
        service.signupUser(signupDto);
        AccountPrincipalDto dto = service.getAccountPrincipal(signupDto.getEmail());
        service.completeSignup(dto.getId(), completeSignupDto);

        try {
            service.deleteAccount(Long.MAX_VALUE);
            Assert.fail("Expected " + SgAccountNotFoundException.class.getName());
        } catch (SgAccountNotFoundException e) {
        }
        
        service.deleteAccount(dto.getId());

        try {
            service.signIn(signinDto);
            Assert.fail("Expected " + SgAccountNotFoundException.class.getName());
        } catch (SgAccountNotFoundException e) {
        }

    }
}
