package com.sg.domain.service;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import com.sg.domain.enumerations.Sex;
import com.sg.dto.request.ThreadCreateDto;
import com.sg.dto.request.ThreadDeleteDto;
import com.sg.dto.request.ThreadUpdateDto;
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
}
