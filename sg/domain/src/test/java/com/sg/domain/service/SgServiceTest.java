package com.sg.domain.service;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import com.sg.domain.enumerations.Sex;
import com.sg.domain.repository.AccountRepository;
import com.sg.domain.repository.CanvasRepository;
import com.sg.domain.repository.ProductRepository;
import com.sg.domain.repository.ThreadRepository;
import com.sg.dto.request.CompleteSignupDto;
import com.sg.dto.request.ResetPasswordDto;
import com.sg.dto.request.SigninDto;
import com.sg.dto.request.SignupDto;
import com.sg.dto.request.UserInfoUpdateDto;
import com.sg.dto.response.UserInfoDto;
import java.math.BigDecimal;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.BeforeClass;
import org.mockito.Mockito;

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


    private static final BigDecimal STITCHES_14 = new BigDecimal(14);
    private static final BigDecimal STITCHES_18 = new BigDecimal(18);

    private static final UserInfoUpdateDto userInfoUpdateDto;
    private static final UserInfoDto userInfoDto;
    private static final String USER_NICK_NAME = "Жека Пират";

    private static final ResetPasswordDto resetPasswordDto;

    static {
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
}
