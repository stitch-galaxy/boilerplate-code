package com.sg.domain.service;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import com.sg.domain.entites.Canvas;
import com.sg.domain.enumerations.Roles;
import com.sg.domain.enumerations.Sex;
import com.sg.dto.request.CanvasCreateDto;
import com.sg.dto.request.CanvasDeleteDto;
import com.sg.dto.request.CanvasUpdateDto;
import com.sg.dto.request.ThreadCreateDto;
import com.sg.dto.request.ThreadDeleteDto;
import com.sg.dto.request.ThreadUpdateDto;
import com.sg.domain.service.SgService;
import com.sg.domain.exception.SgAccountNotFoundException;
import com.sg.domain.exception.SgCanvasAlreadyExistsException;
import com.sg.domain.exception.SgCanvasNotFoundException;
import com.sg.domain.exception.SgEmailNonVerifiedException;
import com.sg.domain.exception.SgInvalidPasswordException;
import com.sg.domain.exception.SgSignupAlreadyCompletedException;
import com.sg.domain.exception.SgSignupForRegisteredButNonVerifiedEmailException;
import com.sg.domain.exception.SgThreadAlreadyExistsException;
import com.sg.domain.exception.SgThreadNotFoundException;
import com.sg.domain.repository.AccountRepository;
import com.sg.domain.repository.CanvasRepository;
import com.sg.domain.repository.ProductRepository;
import com.sg.domain.repository.ThreadRepository;
import com.sg.dto.request.CompleteSignupDto;
import com.sg.dto.request.ResetPasswordDto;
import com.sg.dto.request.SigninDto;
import com.sg.dto.request.SignupDto;
import com.sg.dto.request.UserInfoUpdateDto;
import com.sg.dto.response.AccountRolesDto;
import com.sg.dto.response.CanvasesListDto;
import com.sg.dto.response.ThreadsListDto;
import com.sg.dto.response.UserInfoDto;
import com.sg.domain.entites.Thread;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 *
 * @author tarasev
 */
public class SgServiceTest {

    private static SgService service;

    private static ProductRepository productRepository;
    private static ThreadRepository threadRepository;
    private static CanvasRepository canvasRepository;
    private static AccountRepository accountRepository;

    @BeforeClass
    public static void setupClass() {
        productRepository = Mockito.mock(ProductRepository.class);
        threadRepository = Mockito.mock(ThreadRepository.class);
        canvasRepository = Mockito.mock(CanvasRepository.class);
        accountRepository = Mockito.mock(AccountRepository.class);
        service = new SgServiceImpl(productRepository,
                threadRepository,
                canvasRepository,
                accountRepository);
    }

    @Before
    public void setup() {
        Mockito.reset(productRepository);
        Mockito.reset(threadRepository);
        Mockito.reset(canvasRepository);
        Mockito.reset(accountRepository);
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
        userInfoUpdateDto.setSex(Sex.MALE.name());
        userInfoUpdateDto.setUserBirthDate(USER_BIRTH_DATE);
        userInfoUpdateDto.setUserFirstName(NEW_USER_FIRST_NAME);
        userInfoUpdateDto.setUserLastName(NEW_USER_LAST_NAME);

        userInfoDto = new UserInfoDto();
        userInfoDto.setNickname(USER_NICK_NAME);
        userInfoDto.setSex(Sex.MALE.name());
        userInfoDto.setUserBirthDate(USER_BIRTH_DATE);
        userInfoDto.setUserFirstName(NEW_USER_FIRST_NAME);
        userInfoDto.setUserLastName(NEW_USER_LAST_NAME);

        resetPasswordDto = new ResetPasswordDto();
        resetPasswordDto.setPassword(NEW_USER_PASSWORD);
    }

    @Test
    public void testThreadCreate() {
        ThreadCreateDto dto = new ThreadCreateDto();
        dto.setCode(DMC);
        when(threadRepository.findByCode(DMC)).thenReturn(null);
        service.create(dto);

        verify(threadRepository).findByCode(DMC);
        ArgumentCaptor<Thread> threadCaptor = ArgumentCaptor.forClass(Thread.class);
        verify(threadRepository).save(threadCaptor.capture());
        Assert.assertEquals(DMC, threadCaptor.getValue().getCode());
        Assert.assertNull(threadCaptor.getValue().getId());

    }

    @Test(expected = SgThreadAlreadyExistsException.class)
    public void testThreadCreateThrowSgThreadAlreadyExistsException() {
        ThreadCreateDto dto = new ThreadCreateDto();
        dto.setCode(DMC);
        Thread thread = new Thread();
        thread.setCode(DMC);
        thread.setId(1);
        when(threadRepository.findByCode(DMC)).thenReturn(thread);
        service.create(dmcThreadDto);
    }

    @Test
    public void testThreadsList() {
        Thread dmcThread = new Thread();
        dmcThread.setCode(DMC);
        dmcThread.setId(1);
        Thread anchorThread = new Thread();
        anchorThread.setCode(ANCHOR);
        anchorThread.setId(2);
        List<Thread> allThreads = new ArrayList<Thread>();
        allThreads.add(dmcThread);
        allThreads.add(anchorThread);
        
        when(threadRepository.findAll()).thenReturn(allThreads);
        ThreadsListDto dto = service.listThreads();
        verify(threadRepository).findAll();
        
        Assert.assertEquals(DMC, dto.getThreads().get(0).getCode());
        Assert.assertEquals(ANCHOR, dto.getThreads().get(1).getCode());
    }
    
    @Test(expected = SgThreadNotFoundException.class)
    public void testThreadUpdateThrowsSgThreadNotFoundException() {
        ThreadUpdateDto updateDto = new ThreadUpdateDto();
        updateDto.setRefCode(ANCHOR);
        updateDto.setCode(DMC);
        when(threadRepository.findByCode(ANCHOR)).thenReturn(null);
        service.update(updateDto);
    }
    
    @Test
    public void testThreadUpdate() {
        ThreadUpdateDto updateDto = new ThreadUpdateDto();
        updateDto.setRefCode(ANCHOR);
        updateDto.setCode(DMC);
        
        Thread thread = new Thread();
        thread.setCode(ANCHOR);
        thread.setId(1);
        
        when(threadRepository.findByCode(ANCHOR)).thenReturn(thread);
        service.update(updateDto);
        verify(threadRepository).findByCode(ANCHOR);
        ArgumentCaptor<Thread> threadCaptor = ArgumentCaptor.forClass(Thread.class);
        verify(threadRepository).save(threadCaptor.capture());
        Assert.assertEquals(DMC, threadCaptor.getValue().getCode());
        Assert.assertEquals(1, threadCaptor.getValue().getId().intValue());
    }

    @Test(expected = SgThreadNotFoundException.class)
    public void testThreadDeleteThrowsSgThreadNotFoundException() {
        ThreadDeleteDto ref = new ThreadDeleteDto();
        ref.setCode(ANCHOR);
        when(threadRepository.findByCode(ANCHOR)).thenReturn(null);
        service.delete(ref);
    }
    
    @Test
    public void testThreadDelete() {
        ThreadDeleteDto ref = new ThreadDeleteDto();
        ref.setCode(ANCHOR);
        Thread thread = new Thread();
        thread.setCode(ANCHOR);
        thread.setId(1);
        when(threadRepository.findByCode(ANCHOR)).thenReturn(thread);
        service.delete(ref);
        verify(threadRepository).findByCode(ANCHOR);
        ArgumentCaptor<Thread> threadCaptor = ArgumentCaptor.forClass(Thread.class);
        verify(threadRepository).delete(threadCaptor.capture());
        Assert.assertEquals(ANCHOR, threadCaptor.getValue().getCode());
        Assert.assertEquals(1, threadCaptor.getValue().getId().intValue());
    }
    
    @Test(expected = SgCanvasAlreadyExistsException.class)
    public void testCanvasCreateThrowSgCanvasAlreadyExistsException() {
        CanvasCreateDto dto = new CanvasCreateDto();
        dto.setCode(AIDA_14);
        dto.setStitchesPerInch(STITCHES_14);
        
        Canvas canvas = new Canvas();
        canvas.setCode(AIDA_14);
        canvas.setStitchesPerInch(STITCHES_14);
        canvas.setId(1);
        
        when(canvasRepository.findByCode(AIDA_14)).thenReturn(canvas);
        service.create(dto);
    }
    
    @Test
    public void testCanvasCreate() {
        CanvasCreateDto dto = new CanvasCreateDto();
        dto.setCode(AIDA_14);
        dto.setStitchesPerInch(STITCHES_14);
        
        when(canvasRepository.findByCode(DMC)).thenReturn(null);
        service.create(dto);
        
        
        verify(canvasRepository).findByCode(AIDA_14);
        ArgumentCaptor<Canvas> canvasCaptor = ArgumentCaptor.forClass(Canvas.class);
        verify(canvasRepository).save(canvasCaptor.capture());
        Assert.assertEquals(AIDA_14, canvasCaptor.getValue().getCode());
        Assert.assertEquals(STITCHES_14, canvasCaptor.getValue().getStitchesPerInch());
        Assert.assertNull(canvasCaptor.getValue().getId());
    }

    @Test
    public void testCanvasesList() {
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
    public void testCanvasUpdate() {
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
    public void testCanvasDelete() {
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
    public void testGetAccountIdThrowsExceptionIfAccountNotFound() {
        try {
            service.getAccountId(signupDto.getEmail());
            Assert.fail("Expected  " + SgAccountNotFoundException.class.getName());
        } catch (SgAccountNotFoundException e) {
        }
    }

    @Test
    public void testSignupAdmin() {
        service.signupAdmin(signupDto);

        Long accountId = service.getAccountId(signupDto.getEmail());
        AccountRolesDto rolesDto = service.getAccountRoles(accountId);
        //TODO: think how to test it properly
        //Assert.assertEquals(Boolean.FALSE, accountPrincipalDto.getEmailVerified());

        Assert.assertTrue(rolesDto.getRoles().size() == 2);
        Assert.assertTrue(rolesDto.getRoles().contains(Roles.USER));
        Assert.assertTrue(rolesDto.getRoles().contains(Roles.ADMIN));

        try {
            service.signupUser(signupDto);
            Assert.fail("Expected " + SgSignupForRegisteredButNonVerifiedEmailException.class.getName());
        } catch (SgSignupForRegisteredButNonVerifiedEmailException e) {
        }

        service.completeSignup(accountId, completeSignupDto);

        try {
            service.signupUser(signupDto);
            Assert.fail("Expected " + SgSignupAlreadyCompletedException.class.getName());
        } catch (SgSignupAlreadyCompletedException e) {
        }
    }

    @Test
    public void testSignupUser() {
        service.signupUser(signupDto);

        Long accountId = service.getAccountId(signupDto.getEmail());
        AccountRolesDto rolesDto = service.getAccountRoles(accountId);
        //TODO: think how to test it properly
        //Assert.assertEquals(Boolean.FALSE, accountPrincipalDto.getEmailVerified());

        Assert.assertTrue(rolesDto.getRoles().size() == 1);
        Assert.assertTrue(rolesDto.getRoles().contains(Roles.USER));

        try {
            service.signupUser(signupDto);
            Assert.fail("Expected " + SgSignupForRegisteredButNonVerifiedEmailException.class.getName());
        } catch (SgSignupForRegisteredButNonVerifiedEmailException e) {
        }

        service.completeSignup(accountId, completeSignupDto);

        try {
            service.signupUser(signupDto);
            Assert.fail("Expected " + SgSignupAlreadyCompletedException.class.getName());
        } catch (SgSignupAlreadyCompletedException e) {
        }
    }

    @Test
    public void testCompleteSignup() {
        try {
            service.completeSignup(1L, completeSignupDto);
            Assert.fail("Expected " + SgAccountNotFoundException.class.getName());
        } catch (SgAccountNotFoundException e) {
        }

        service.signupUser(signupDto);

        Long accountId = service.getAccountId(signupDto.getEmail());

        service.completeSignup(accountId, completeSignupDto);

        try {
            service.completeSignup(accountId, completeSignupDto);
            Assert.fail("Expected " + SgSignupAlreadyCompletedException.class.getName());
        } catch (SgSignupAlreadyCompletedException e) {
        }
    }

    @Test
    public void testSignin() {
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

        Long accountId = service.getAccountId(signupDto.getEmail());

        service.completeSignup(accountId, completeSignupDto);

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

    @Test
    public void testSetGetUserInformation() {
        service.signupUser(signupDto);
        Long accountId = service.getAccountId(signupDto.getEmail());
        service.completeSignup(accountId, completeSignupDto);

        try {
            service.getUserInfo(Long.MAX_VALUE);
            Assert.fail("Expected " + SgAccountNotFoundException.class.getName());
        } catch (SgAccountNotFoundException e) {
        }

        UserInfoDto userInfo = service.getUserInfo(accountId);
        Assert.assertEquals(USER_FIRST_NAME, userInfo.getUserFirstName());
        Assert.assertEquals(USER_LAST_NAME, userInfo.getUserLastName());
        Assert.assertEquals(null, userInfo.getUserBirthDate());
        Assert.assertEquals(null, userInfo.getNickname());

        service.setUserInfo(accountId, userInfoUpdateDto);
        userInfo = service.getUserInfo(accountId);
        Assert.assertEquals(userInfoDto, userInfo);

    }

    @Test
    public void testResetPassword() {
        //TODO: when signin with FB will be completed cover with test: SgAccountWithoutEmailException

        service.signupUser(signupDto);
        Long accountId = service.getAccountId(signupDto.getEmail());
        try {
            service.resetPassword(accountId, resetPasswordDto);
            Assert.fail("Expected " + SgEmailNonVerifiedException.class.getName());
        } catch (SgEmailNonVerifiedException e) {
        }

        service.completeSignup(accountId, completeSignupDto);

        try {
            service.resetPassword(Long.MIN_VALUE, resetPasswordDto);
            Assert.fail("Expected " + SgAccountNotFoundException.class.getName());
        } catch (SgAccountNotFoundException e) {
        }

        service.resetPassword(accountId, resetPasswordDto);
        SigninDto signInWithNewPassword = new SigninDto();
        signInWithNewPassword.setPassword(NEW_USER_PASSWORD);
        signInWithNewPassword.setEmail(USER_EMAIL);
        service.signIn(signInWithNewPassword);
    }

    @Test
    public void testDeleteAccount() {
        service.signupUser(signupDto);
        Long accountId = service.getAccountId(signupDto.getEmail());
        service.completeSignup(accountId, completeSignupDto);

        try {
            service.deleteAccount(Long.MAX_VALUE);
            Assert.fail("Expected " + SgAccountNotFoundException.class.getName());
        } catch (SgAccountNotFoundException e) {
        }

        service.deleteAccount(accountId);

        try {
            service.signIn(signinDto);
            Assert.fail("Expected " + SgAccountNotFoundException.class.getName());
        } catch (SgAccountNotFoundException e) {
        }

    }
}
