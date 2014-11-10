package com.sg.domain.test.jpa;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import com.sg.constants.DtoFieldCodes;
import com.sg.domain.service.SgService;
import com.sg.rest.dto.validator.components.ValidatorComponent;
import com.sg.domain.service.exception.SgDataValidationException;
import com.sg.domain.service.jpa.spring.PersistenceContextConfig;
import com.sg.domain.service.jpa.spring.ServiceContextConfig;
import com.sg.domain.service.jpa.spring.ValidatorContextConfig;
import com.sg.domain.test.spring.configuration.TestJpaServicePropertiesContextConfiguration;
import com.sg.dto.request.CanvasCreateDto;
import com.sg.dto.request.CanvasDeleteDto;
import com.sg.dto.request.CanvasUpdateDto;
import com.sg.dto.request.CompleteSignupDto;
import com.sg.dto.request.ResetPasswordDto;
import com.sg.dto.request.SigninDto;
import com.sg.dto.request.SignupDto;
import com.sg.dto.request.ThreadCreateDto;
import com.sg.dto.request.ThreadDeleteDto;
import com.sg.dto.request.ThreadUpdateDto;
import com.sg.dto.request.UserInfoUpdateDto;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;
import org.joda.time.Instant;
import org.joda.time.LocalDate;
import org.junit.Assert;
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
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = {TestJpaServicePropertiesContextConfiguration.class, ValidatorContextConfig.class, PersistenceContextConfig.class, ServiceContextConfig.class})
public class DtoValidationsTest {

    @Autowired
    private SgService service;

    @Autowired
    private ValidatorComponent validatorComponent;

    private static final String INVALID_EMAIL_NULL = null;
    private static final String INVALID_EMAIL_1 = "abc@gmail.com ";
    private static final String INVALID_EMAIL_2 = " abc@gmail.com";
    private static final String INVALID_EMAIL_3 = "abc";
    private static final String INVALID_EMAIL_4 = "Тарасов@почта.рф ";
    private static final String INVALID_EMAIL_5 = " Тарасов@почта.рф";
    private static final String INVALID_EMAIL_6 = "тарасов";
    private static final String VALID_EMAIL_1 = "tarasov.e.a@gmail.com";
    private static final String VALID_EMAIL_2 = "тарасов@почта.рф";

    private static final String INVALID_USER_FIRST_NAME_NULL = null;
    private static final String INVALID_USER_FIRST_NAME_EMPTY = "";
    private static final String VALID_USER_FIRST_NAME_1 = "У";
    private static final String VALID_USER_FIRST_NAME_2 = "Evgeny";

    private static final String INVALID_USER_LAST_NAME_NULL = null;
    private static final String INVALID_USER_LAST_NAME_EMPTY = "";
    private static final String VALID_USER_LAST_NAME_1 = "Н";
    private static final String VALID_USER_LAST_NAME_2 = "Tarasov";

    private static final String INVALID_PASSWORD_NULL = null;
    private static final String INVALID_PASSWORD_1 = "У";
    private static final String INVALID_PASSWORD_2 = "tara";
    private static final String VALID_PASSWORD_1 = "taras";
    private static final String VALID_PASSWORD_2 = "тарас";

    private static final String INVALID_THREAD_CODE_NULL = null;
    private static final String INVALID_THREAD_CODE_EMPTY = "";
    private static final String VALID_THREAD_CODE = "DMC";

    private static final String INVALID_CANVAS_CODE_NULL = null;
    private static final String INVALID_CANVAS_CODE_EMPTY = "";
    private static final String VALID_CANVAS_CODE = "Aida 14";

    private static final BigDecimal INVALID_CANVAS_STITCHES_PER_INCH_NULL = null;
    private static final BigDecimal VALID_CANVAS_STITCHES_PER_INCH = new BigDecimal(14);

    private static final LocalDate VALID_USER_BIRTH_DATE_NULL = null;
    private static final LocalDate VALID_USER_BIRTH_DATE_1 = Instant.now().toDateTime().toLocalDate().minusDays(1);
    private static final LocalDate INVALID_USER_BIRTH_DATE = Instant.now().toDateTime().toLocalDate().plusDays(1);

    @Test
    public void testInvalidSignupDtoEmail() throws SgDataValidationException {
        testInvalidSignupDtoEmailImpl(INVALID_EMAIL_NULL);
        testInvalidSignupDtoEmailImpl(INVALID_EMAIL_1);
        testInvalidSignupDtoEmailImpl(INVALID_EMAIL_2);
        testInvalidSignupDtoEmailImpl(INVALID_EMAIL_3);
        testInvalidSignupDtoEmailImpl(INVALID_EMAIL_4);
        testInvalidSignupDtoEmailImpl(INVALID_EMAIL_5);
        testInvalidSignupDtoEmailImpl(INVALID_EMAIL_6);
    }

    private void testInvalidSignupDtoEmailImpl(String email) {
        SignupDto dto = new SignupDto();
        dto.setEmail(email);
        try {
            validatorComponent.validate(dto);
            Assert.fail("Expected " + SgDataValidationException.class.getName());
        } catch (SgDataValidationException e) {
            Assert.assertTrue(e.getFieldErrors().contains(DtoFieldCodes.FIELD_SIGNUP_DTO_USER_EMAIL));
        }
    }

    @Test
    public void testValidSignupDtoEmail() throws SgDataValidationException {
        testValidSignupDtoEmailImpl(VALID_EMAIL_1);
        testValidSignupDtoEmailImpl(VALID_EMAIL_2);
    }

    private void testValidSignupDtoEmailImpl(String email) throws SgDataValidationException {
        SignupDto dto = new SignupDto();
        dto.setEmail(email);
        dto.setUserFirstName(VALID_USER_FIRST_NAME_1);
        dto.setUserLastName(VALID_USER_LAST_NAME_1);
        validatorComponent.validate(dto);
    }

    @Test
    public void testInvalidSignupDtoUserFirstName() throws SgDataValidationException {
        testInvalidSignupDtoUserFirstNameImpl(INVALID_USER_FIRST_NAME_NULL);
        testInvalidSignupDtoUserFirstNameImpl(INVALID_USER_FIRST_NAME_EMPTY);
    }

    private void testInvalidSignupDtoUserFirstNameImpl(String firstName) {
        SignupDto dto = new SignupDto();
        dto.setUserFirstName(firstName);
        try {
            validatorComponent.validate(dto);
            Assert.fail("Expected " + SgDataValidationException.class.getName());
        } catch (SgDataValidationException e) {
            Assert.assertTrue(e.getFieldErrors().contains(DtoFieldCodes.FIELD_SIGNUP_DTO_USER_FIRST_NAME));
        }
    }

    @Test
    public void testValidSignupDtoUserFirstName() throws SgDataValidationException {
        testValidSignupDtoUserFirstNameImpl(VALID_USER_FIRST_NAME_1);
        testValidSignupDtoUserFirstNameImpl(VALID_USER_FIRST_NAME_2);
    }

    private void testValidSignupDtoUserFirstNameImpl(String firstName) throws SgDataValidationException {
        SignupDto dto = new SignupDto();
        dto.setEmail(VALID_EMAIL_1);
        dto.setUserLastName(VALID_USER_LAST_NAME_1);
        dto.setUserFirstName(firstName);
        validatorComponent.validate(dto);
    }

    @Test
    public void testInvalidSignupDtoUserLastName() throws SgDataValidationException {
        testInvalidSignupDtoUserLastNameImpl(INVALID_USER_LAST_NAME_NULL);
        testInvalidSignupDtoUserLastNameImpl(INVALID_USER_LAST_NAME_EMPTY);
    }

    private void testInvalidSignupDtoUserLastNameImpl(String lastName) {
        SignupDto dto = new SignupDto();
        dto.setUserLastName(lastName);
        try {
            validatorComponent.validate(dto);
            Assert.fail("Expected " + SgDataValidationException.class.getName());
        } catch (SgDataValidationException e) {
            Assert.assertTrue(e.getFieldErrors().contains(DtoFieldCodes.FIELD_SIGNUP_DTO_USER_LAST_NAME));
        }
    }

    @Test
    public void testValidSignupDtoUserLastName() throws SgDataValidationException {
        testValidSignupDtoUserLastNameImpl(VALID_USER_LAST_NAME_1);
        testValidSignupDtoUserLastNameImpl(VALID_USER_LAST_NAME_2);
    }

    private void testValidSignupDtoUserLastNameImpl(String lastName) throws SgDataValidationException {
        SignupDto dto = new SignupDto();
        dto.setUserLastName(lastName);
        dto.setEmail(VALID_EMAIL_1);
        dto.setUserFirstName(VALID_USER_FIRST_NAME_1);
        validatorComponent.validate(dto);
    }

    @Test
    public void testInvalidSignupUserServiceCall() throws SgDataValidationException {
        SignupDto dto = new SignupDto();
        dto.setEmail(INVALID_EMAIL_1);
        dto.setUserFirstName(INVALID_USER_FIRST_NAME_EMPTY);
        dto.setUserLastName(INVALID_USER_LAST_NAME_EMPTY);

        try {
            service.signupUser(dto);
            Assert.fail("Expected " + SgDataValidationException.class.getName());
        } catch (SgDataValidationException e) {
            Assert.assertEquals(new HashSet<String>(Arrays.asList(new String[]{
                DtoFieldCodes.FIELD_SIGNUP_DTO_USER_EMAIL,
                DtoFieldCodes.FIELD_SIGNUP_DTO_USER_FIRST_NAME,
                DtoFieldCodes.FIELD_SIGNUP_DTO_USER_LAST_NAME,})),
                    e.getFieldErrors());
        }
    }

    @Test
    public void testInvalidSignupAdminServiceCall() throws SgDataValidationException {
        SignupDto dto = new SignupDto();
        dto.setEmail(INVALID_EMAIL_1);
        dto.setUserFirstName(INVALID_USER_FIRST_NAME_EMPTY);
        dto.setUserLastName(INVALID_USER_LAST_NAME_EMPTY);

        try {
            service.signupAdmin(dto);
            Assert.fail("Expected " + SgDataValidationException.class.getName());
        } catch (SgDataValidationException e) {
            Assert.assertEquals(new HashSet<String>(Arrays.asList(new String[]{
                DtoFieldCodes.FIELD_SIGNUP_DTO_USER_EMAIL,
                DtoFieldCodes.FIELD_SIGNUP_DTO_USER_FIRST_NAME,
                DtoFieldCodes.FIELD_SIGNUP_DTO_USER_LAST_NAME,})),
                    e.getFieldErrors());
        }
    }

    @Test
    public void testInvalidCompleteSignupDtoPassword() throws SgDataValidationException {
        testInvalidCompleteSignupDtoPasswordImpl(INVALID_PASSWORD_NULL);
        testInvalidCompleteSignupDtoPasswordImpl(INVALID_PASSWORD_1);
        testInvalidCompleteSignupDtoPasswordImpl(INVALID_PASSWORD_2);
    }

    private void testInvalidCompleteSignupDtoPasswordImpl(String password) {
        CompleteSignupDto dto = new CompleteSignupDto();
        dto.setPassword(password);
        try {
            validatorComponent.validate(dto);
            Assert.fail("Expected " + SgDataValidationException.class.getName());
        } catch (SgDataValidationException e) {
            Assert.assertTrue(e.getFieldErrors().contains(DtoFieldCodes.FIELD_COMPLETE_SIGNUP_DTO_PASSWORD));
        }
    }

    @Test
    public void testValidCompleteSignupDtoPassword() throws SgDataValidationException {
        testValidCompleteSignupDtoPasswordImpl(VALID_PASSWORD_1);
        testValidCompleteSignupDtoPasswordImpl(VALID_PASSWORD_2);
    }

    private void testValidCompleteSignupDtoPasswordImpl(String password) throws SgDataValidationException {
        CompleteSignupDto dto = new CompleteSignupDto();
        dto.setPassword(password);
        validatorComponent.validate(dto);
    }

    @Test
    public void testInvalidCompeleSignupServiceCall() throws SgDataValidationException {
        CompleteSignupDto dto = new CompleteSignupDto();
        dto.setPassword(INVALID_PASSWORD_1);

        try {
            service.completeSignup(1L, dto);
            Assert.fail("Expected " + SgDataValidationException.class.getName());
        } catch (SgDataValidationException e) {
            Assert.assertEquals(new HashSet<String>(Arrays.asList(new String[]{
                DtoFieldCodes.FIELD_COMPLETE_SIGNUP_DTO_PASSWORD,})),
                    e.getFieldErrors());
        }
    }

    @Test
    public void testInvalidSigninDtoPassword() throws SgDataValidationException {
        testInvalidSigninDtoPasswordImpl(INVALID_PASSWORD_NULL);
    }

    private void testInvalidSigninDtoPasswordImpl(String password) {
        SigninDto dto = new SigninDto();
        dto.setPassword(password);
        try {
            validatorComponent.validate(dto);
            Assert.fail("Expected " + SgDataValidationException.class.getName());
        } catch (SgDataValidationException e) {
            Assert.assertTrue(e.getFieldErrors().contains(DtoFieldCodes.FIELD_SIGNIN_DTO_PASSWORD));
        }
    }

    @Test
    public void testValidSignInDtoPassword() throws SgDataValidationException {
        testValidSignInDtoPasswordImpl(VALID_PASSWORD_1);
    }

    private void testValidSignInDtoPasswordImpl(String password) throws SgDataValidationException {
        SigninDto dto = new SigninDto();
        dto.setPassword(password);
        dto.setEmail(VALID_EMAIL_1);
        validatorComponent.validate(dto);
    }

    @Test
    public void testInvalidSigninDtoEmail() throws SgDataValidationException {
        testInvalidSigninDtoEmailImpl(INVALID_EMAIL_NULL);
        testInvalidSigninDtoEmailImpl(INVALID_EMAIL_1);
        testInvalidSigninDtoEmailImpl(INVALID_EMAIL_2);
        testInvalidSigninDtoEmailImpl(INVALID_EMAIL_3);
        testInvalidSigninDtoEmailImpl(INVALID_EMAIL_4);
        testInvalidSigninDtoEmailImpl(INVALID_EMAIL_5);
        testInvalidSigninDtoEmailImpl(INVALID_EMAIL_6);
    }

    private void testInvalidSigninDtoEmailImpl(String email) {
        SigninDto dto = new SigninDto();
        dto.setEmail(email);
        try {
            validatorComponent.validate(dto);
            Assert.fail("Expected " + SgDataValidationException.class.getName());
        } catch (SgDataValidationException e) {
            Assert.assertTrue(e.getFieldErrors().contains(DtoFieldCodes.FIELD_SIGNIN_DTO_EMAIL));
        }
    }

    @Test
    public void testValidSignInDtoEmail() throws SgDataValidationException {
        testValidSignInDtoEmaiImpl(VALID_EMAIL_1);
        testValidSignInDtoEmaiImpl(VALID_EMAIL_2);
    }

    private void testValidSignInDtoEmaiImpl(String email) throws SgDataValidationException {
        SigninDto dto = new SigninDto();
        dto.setEmail(email);
        dto.setPassword(VALID_PASSWORD_1);
        validatorComponent.validate(dto);
    }

    @Test
    public void testInvalidSigninServiceCall() throws SgDataValidationException {
        SigninDto dto = new SigninDto();
        dto.setPassword(INVALID_PASSWORD_NULL);
        dto.setEmail(INVALID_EMAIL_1);

        try {
            service.signIn(dto);
            Assert.fail("Expected " + SgDataValidationException.class.getName());
        } catch (SgDataValidationException e) {
            Assert.assertEquals(new HashSet<String>(Arrays.asList(new String[]{
                DtoFieldCodes.FIELD_SIGNIN_DTO_EMAIL,
                DtoFieldCodes.FIELD_SIGNIN_DTO_PASSWORD,})),
                    e.getFieldErrors());
        }
    }

    @Test
    public void testInvalidThreadCreateDtoCode() throws SgDataValidationException {
        testInvalidThreadCreateDtoCodeImpl(INVALID_THREAD_CODE_NULL);
        testInvalidThreadCreateDtoCodeImpl(INVALID_THREAD_CODE_EMPTY);
    }

    private void testInvalidThreadCreateDtoCodeImpl(String code) {
        ThreadCreateDto dto = new ThreadCreateDto();
        dto.setCode(code);
        try {
            validatorComponent.validate(dto);
            Assert.fail("Expected " + SgDataValidationException.class.getName());
        } catch (SgDataValidationException e) {
            Assert.assertTrue(e.getFieldErrors().contains(DtoFieldCodes.FIELD_THREAD_CREATE_DTO_CODE));
        }
    }

    @Test
    public void testValidThreadCreateDtoCode() throws SgDataValidationException {
        testValidThreadCreateDtoCodeImpl(VALID_THREAD_CODE);
    }

    private void testValidThreadCreateDtoCodeImpl(String code) throws SgDataValidationException {
        ThreadCreateDto dto = new ThreadCreateDto();
        dto.setCode(code);
        validatorComponent.validate(dto);
    }

    @Test
    public void testInvalidThreadCreateServiceCall() throws SgDataValidationException {
        ThreadCreateDto dto = new ThreadCreateDto();
        dto.setCode(INVALID_THREAD_CODE_EMPTY);

        try {
            service.create(dto);
            Assert.fail("Expected " + SgDataValidationException.class.getName());
        } catch (SgDataValidationException e) {
            Assert.assertEquals(new HashSet<String>(Arrays.asList(new String[]{
                DtoFieldCodes.FIELD_THREAD_CREATE_DTO_CODE,})),
                    e.getFieldErrors());
        }
    }

    @Test
    public void testInvalidCanvasCreateDtoCode() throws SgDataValidationException {
        testInvalidCanvasCreateDtoCodeImpl(INVALID_CANVAS_CODE_NULL);
        testInvalidCanvasCreateDtoCodeImpl(INVALID_CANVAS_CODE_EMPTY);
    }

    private void testInvalidCanvasCreateDtoCodeImpl(String code) {
        CanvasCreateDto dto = new CanvasCreateDto();
        dto.setCode(code);
        try {
            validatorComponent.validate(dto);
            Assert.fail("Expected " + SgDataValidationException.class.getName());
        } catch (SgDataValidationException e) {
            Assert.assertTrue(e.getFieldErrors().contains(DtoFieldCodes.FIELD_CREATE_CANVAS_DTO_CODE));
        }
    }

    @Test
    public void testValidCanvasCreateDtoCode() throws SgDataValidationException {
        testValidCanvasCreateDtoCodeImpl(VALID_CANVAS_CODE);
    }

    private void testValidCanvasCreateDtoCodeImpl(String code) throws SgDataValidationException {
        CanvasCreateDto dto = new CanvasCreateDto();
        dto.setCode(code);
        dto.setStitchesPerInch(VALID_CANVAS_STITCHES_PER_INCH);
        validatorComponent.validate(dto);
    }

    @Test
    public void testInvalidCanvasCreateDtoStitchesPerInch() throws SgDataValidationException {
        testInvalidCanvasCreateDtoStitchesPerInchImpl(INVALID_CANVAS_STITCHES_PER_INCH_NULL);
    }

    private void testInvalidCanvasCreateDtoStitchesPerInchImpl(BigDecimal stitchesPerInch) {
        CanvasCreateDto dto = new CanvasCreateDto();
        dto.setStitchesPerInch(stitchesPerInch);
        try {
            validatorComponent.validate(dto);
            Assert.fail("Expected " + SgDataValidationException.class.getName());
        } catch (SgDataValidationException e) {
            Assert.assertTrue(e.getFieldErrors().contains(DtoFieldCodes.FIELD_CREATE_CANVAS_DTO_STITCHES_PER_INCH));
        }
    }

    @Test
    public void testValidCanvasCreateDtoStitchesPerInch() throws SgDataValidationException {
        testValidCanvasCreateDtoStitchesPerInchImpl(VALID_CANVAS_STITCHES_PER_INCH);
    }

    private void testValidCanvasCreateDtoStitchesPerInchImpl(BigDecimal stitchesPerInch) throws SgDataValidationException {
        CanvasCreateDto dto = new CanvasCreateDto();
        dto.setStitchesPerInch(stitchesPerInch);
        dto.setCode(VALID_CANVAS_CODE);
        validatorComponent.validate(dto);
    }

    @Test
    public void testInvalidCanvasCreateServiceCall() throws SgDataValidationException {
        CanvasCreateDto dto = new CanvasCreateDto();
        dto.setCode(INVALID_CANVAS_CODE_EMPTY);

        try {
            service.create(dto);
            Assert.fail("Expected " + SgDataValidationException.class.getName());
        } catch (SgDataValidationException e) {
            Assert.assertEquals(new HashSet<String>(Arrays.asList(new String[]{
                DtoFieldCodes.FIELD_CREATE_CANVAS_DTO_CODE,
                DtoFieldCodes.FIELD_CREATE_CANVAS_DTO_STITCHES_PER_INCH,})),
                    e.getFieldErrors());
        }
    }

    @Test
    public void testInvalidThreadDeleteDtoCode() throws SgDataValidationException {
        testInvalidThreadDeleteDtoCodeImpl(INVALID_THREAD_CODE_NULL);
        testInvalidThreadDeleteDtoCodeImpl(INVALID_THREAD_CODE_EMPTY);
    }

    private void testInvalidThreadDeleteDtoCodeImpl(String code) {
        ThreadDeleteDto dto = new ThreadDeleteDto();
        dto.setCode(code);
        try {
            validatorComponent.validate(dto);
            Assert.fail("Expected " + SgDataValidationException.class.getName());
        } catch (SgDataValidationException e) {
            Assert.assertTrue(e.getFieldErrors().contains(DtoFieldCodes.FIELD_THREAD_DELETE_DTO_CODE));
        }
    }

    @Test
    public void testValidThreadDeleteDtoCode() throws SgDataValidationException {
        testValidThreadDeleteDtoCodeImpl(VALID_THREAD_CODE);
    }

    private void testValidThreadDeleteDtoCodeImpl(String code) throws SgDataValidationException {
        ThreadDeleteDto dto = new ThreadDeleteDto();
        dto.setCode(code);
        validatorComponent.validate(dto);
    }

    @Test
    public void testInvalidThreadDeleteServiceCall() throws SgDataValidationException {
        ThreadDeleteDto dto = new ThreadDeleteDto();
        dto.setCode(INVALID_THREAD_CODE_EMPTY);

        try {
            service.delete(dto);
            Assert.fail("Expected " + SgDataValidationException.class.getName());
        } catch (SgDataValidationException e) {
            Assert.assertEquals(new HashSet<String>(Arrays.asList(new String[]{
                DtoFieldCodes.FIELD_THREAD_DELETE_DTO_CODE,})),
                    e.getFieldErrors());
        }
    }

    @Test
    public void testInvalidCanvasDeleteDtoCode() throws SgDataValidationException {
        testInvalidCanvasDeleteDtoCodeImpl(INVALID_CANVAS_CODE_NULL);
        testInvalidCanvasDeleteDtoCodeImpl(INVALID_CANVAS_CODE_EMPTY);
    }

    private void testInvalidCanvasDeleteDtoCodeImpl(String code) {
        CanvasDeleteDto dto = new CanvasDeleteDto();
        dto.setCode(code);
        try {
            validatorComponent.validate(dto);
            Assert.fail("Expected " + SgDataValidationException.class.getName());
        } catch (SgDataValidationException e) {
            Assert.assertTrue(e.getFieldErrors().contains(DtoFieldCodes.FIELD_DELETE_CANVAS_DTO_CODE));
        }
    }

    @Test
    public void testValidCanvasDeleteDtoCode() throws SgDataValidationException {
        testValidCanvasDeleteDtoCodeImpl(VALID_CANVAS_CODE);
    }

    private void testValidCanvasDeleteDtoCodeImpl(String code) throws SgDataValidationException {
        CanvasDeleteDto dto = new CanvasDeleteDto();
        dto.setCode(code);
        validatorComponent.validate(dto);
    }

    @Test
    public void testInvalidCanvasDeleteServiceCall() throws SgDataValidationException {
        CanvasDeleteDto dto = new CanvasDeleteDto();
        dto.setCode(INVALID_CANVAS_CODE_EMPTY);

        try {
            service.delete(dto);
            Assert.fail("Expected " + SgDataValidationException.class.getName());
        } catch (SgDataValidationException e) {
            Assert.assertEquals(new HashSet<String>(Arrays.asList(new String[]{
                DtoFieldCodes.FIELD_DELETE_CANVAS_DTO_CODE,})),
                    e.getFieldErrors());
        }
    }

    @Test
    public void testInvalidThreadUpdateDtoCode() throws SgDataValidationException {
        testInvalidThreadUpdateDtoCodeImpl(INVALID_THREAD_CODE_NULL);
        testInvalidThreadUpdateDtoCodeImpl(INVALID_THREAD_CODE_EMPTY);
    }

    private void testInvalidThreadUpdateDtoCodeImpl(String code) {
        ThreadUpdateDto dto = new ThreadUpdateDto();
        dto.setCode(code);
        try {
            validatorComponent.validate(dto);
            Assert.fail("Expected " + SgDataValidationException.class.getName());
        } catch (SgDataValidationException e) {
            Assert.assertTrue(e.getFieldErrors().contains(DtoFieldCodes.FIELD_THREAD_UPDATE_DTO_CODE));
        }
    }

    @Test
    public void testValidThreadUpdateDtoCode() throws SgDataValidationException {
        testValidThreadUpdateDtoCodeImpl(VALID_THREAD_CODE);
    }

    private void testValidThreadUpdateDtoCodeImpl(String code) throws SgDataValidationException {
        ThreadUpdateDto dto = new ThreadUpdateDto();
        dto.setCode(code);
        dto.setRefCode(VALID_THREAD_CODE);
        validatorComponent.validate(dto);
    }

    @Test
    public void testInvalidThreadUpdateDtoRefCode() throws SgDataValidationException {
        testInvalidThreadUpdateDtoRefCodeImpl(INVALID_THREAD_CODE_NULL);
        testInvalidThreadUpdateDtoRefCodeImpl(INVALID_THREAD_CODE_EMPTY);
    }

    private void testInvalidThreadUpdateDtoRefCodeImpl(String code) {
        ThreadUpdateDto dto = new ThreadUpdateDto();
        dto.setRefCode(code);
        try {
            validatorComponent.validate(dto);
            Assert.fail("Expected " + SgDataValidationException.class.getName());
        } catch (SgDataValidationException e) {
            Assert.assertTrue(e.getFieldErrors().contains(DtoFieldCodes.FIELD_THREAD_UPDATE_DTO_REF_CODE));
        }
    }

    @Test
    public void testValidThreadUpdateDtoRefCode() throws SgDataValidationException {
        testValidThreadUpdateDtoRefCodeImpl(VALID_THREAD_CODE);
    }

    private void testValidThreadUpdateDtoRefCodeImpl(String code) throws SgDataValidationException {
        ThreadUpdateDto dto = new ThreadUpdateDto();
        dto.setRefCode(code);
        dto.setCode(VALID_THREAD_CODE);
        validatorComponent.validate(dto);
    }

    @Test
    public void testInvalidThreadUpdateServiceCall() throws SgDataValidationException {
        ThreadUpdateDto dto = new ThreadUpdateDto();
        dto.setCode(INVALID_THREAD_CODE_EMPTY);
        dto.setRefCode(INVALID_THREAD_CODE_EMPTY);

        try {
            service.update(dto);
            Assert.fail("Expected " + SgDataValidationException.class.getName());
        } catch (SgDataValidationException e) {
            Assert.assertEquals(new HashSet<String>(Arrays.asList(new String[]{
                DtoFieldCodes.FIELD_THREAD_UPDATE_DTO_CODE,
                DtoFieldCodes.FIELD_THREAD_UPDATE_DTO_REF_CODE,})),
                    e.getFieldErrors());
        }
    }

    @Test
    public void testInvalidCanvasUpdateDtoCode() throws SgDataValidationException {
        testInvalidCanvasUpdateDtoCodeImpl(INVALID_CANVAS_CODE_NULL);
        testInvalidCanvasUpdateDtoCodeImpl(INVALID_CANVAS_CODE_EMPTY);
    }

    private void testInvalidCanvasUpdateDtoCodeImpl(String code) {
        CanvasUpdateDto dto = new CanvasUpdateDto();
        dto.setCode(code);
        try {
            validatorComponent.validate(dto);
            Assert.fail("Expected " + SgDataValidationException.class.getName());
        } catch (SgDataValidationException e) {
            Assert.assertTrue(e.getFieldErrors().contains(DtoFieldCodes.FIELD_UPDATE_CANVAS_DTO_CODE));
        }
    }

    @Test
    public void testValidCanvasUpdateDtoCode() throws SgDataValidationException {
        testValidCanvasUpdateDtoCodeImpl(VALID_CANVAS_CODE);
    }

    private void testValidCanvasUpdateDtoCodeImpl(String code) throws SgDataValidationException {
        CanvasUpdateDto dto = new CanvasUpdateDto();
        dto.setCode(code);
        dto.setRefCode(VALID_CANVAS_CODE);
        dto.setStitchesPerInch(VALID_CANVAS_STITCHES_PER_INCH);
        validatorComponent.validate(dto);
    }

    @Test
    public void testInvalidCanvasUpdateDtoRefCode() throws SgDataValidationException {
        testInvalidCanvasUpdateDtoRefCodeImpl(INVALID_CANVAS_CODE_NULL);
        testInvalidCanvasUpdateDtoRefCodeImpl(INVALID_CANVAS_CODE_EMPTY);
    }

    private void testInvalidCanvasUpdateDtoRefCodeImpl(String code) {
        CanvasUpdateDto dto = new CanvasUpdateDto();
        dto.setRefCode(code);
        try {
            validatorComponent.validate(dto);
            Assert.fail("Expected " + SgDataValidationException.class.getName());
        } catch (SgDataValidationException e) {
            Assert.assertTrue(e.getFieldErrors().contains(DtoFieldCodes.FIELD_UPDATE_CANVAS_DTO_REF_CODE));
        }
    }

    @Test
    public void testValidCanvasUpdateDtoRefCode() throws SgDataValidationException {
        testValidCanvasUpdateDtoRefCodeImpl(VALID_CANVAS_CODE);
    }

    private void testValidCanvasUpdateDtoRefCodeImpl(String code) throws SgDataValidationException {
        CanvasUpdateDto dto = new CanvasUpdateDto();
        dto.setRefCode(code);
        dto.setCode(VALID_CANVAS_CODE);
        dto.setStitchesPerInch(VALID_CANVAS_STITCHES_PER_INCH);
        validatorComponent.validate(dto);
    }

    @Test
    public void testInvalidCanvasUpdateDtoStitchesPerInch() throws SgDataValidationException {
        testInvalidCanvasUpdateDtoStitchesPerInchImpl(INVALID_CANVAS_STITCHES_PER_INCH_NULL);
    }

    private void testInvalidCanvasUpdateDtoStitchesPerInchImpl(BigDecimal stitchesPerInch) {
        CanvasUpdateDto dto = new CanvasUpdateDto();
        dto.setStitchesPerInch(stitchesPerInch);
        try {
            validatorComponent.validate(dto);
            Assert.fail("Expected " + SgDataValidationException.class.getName());
        } catch (SgDataValidationException e) {
            Assert.assertTrue(e.getFieldErrors().contains(DtoFieldCodes.FIELD_UPDATE_CANVAS_DTO_STITCHES_PER_INCH));
        }
    }

    @Test
    public void testValidCanvasUpdateDtoStitchesPerInch() throws SgDataValidationException {
        testValidCanvasUpdateDtoStitchesPerInchImpl(VALID_CANVAS_STITCHES_PER_INCH);
    }

    private void testValidCanvasUpdateDtoStitchesPerInchImpl(BigDecimal stitchesPerInch) throws SgDataValidationException {
        CanvasUpdateDto dto = new CanvasUpdateDto();
        dto.setStitchesPerInch(stitchesPerInch);
        dto.setCode(VALID_CANVAS_CODE);
        dto.setRefCode(VALID_CANVAS_CODE);
        validatorComponent.validate(dto);
    }

    @Test
    public void testInvalidCanvasUpdateServiceCall() throws SgDataValidationException {
        CanvasUpdateDto dto = new CanvasUpdateDto();
        dto.setCode(INVALID_CANVAS_CODE_EMPTY);

        try {
            service.update(dto);
            Assert.fail("Expected " + SgDataValidationException.class.getName());
        } catch (SgDataValidationException e) {
            Assert.assertEquals(new HashSet<String>(Arrays.asList(new String[]{
                DtoFieldCodes.FIELD_UPDATE_CANVAS_DTO_CODE,
                DtoFieldCodes.FIELD_UPDATE_CANVAS_DTO_REF_CODE,
                DtoFieldCodes.FIELD_UPDATE_CANVAS_DTO_STITCHES_PER_INCH,})),
                    e.getFieldErrors());
        }
    }

    @Test
    public void testValidUserInfoUpdateDtoBirthDate() throws SgDataValidationException {
        testValidUserInfoUpdateDtoBirthDateImpl(VALID_USER_BIRTH_DATE_NULL);
        testValidUserInfoUpdateDtoBirthDateImpl(VALID_USER_BIRTH_DATE_1);
    }

    private void testValidUserInfoUpdateDtoBirthDateImpl(LocalDate date) throws SgDataValidationException {
        UserInfoUpdateDto dto = new UserInfoUpdateDto();
        dto.setUserBirthDate(date);
        validatorComponent.validate(dto);
    }

    @Test
    public void testInvalidUserInfoUpdateDtoRefBirthDate() throws SgDataValidationException {
        testInvalidUserInfoUpdateDtoRefBirthDateImpl(INVALID_USER_BIRTH_DATE);
    }

    private void testInvalidUserInfoUpdateDtoRefBirthDateImpl(LocalDate date) {
        UserInfoUpdateDto dto = new UserInfoUpdateDto();
        dto.setUserBirthDate(date);
        try {
            validatorComponent.validate(dto);
            Assert.fail("Expected " + SgDataValidationException.class.getName());
        } catch (SgDataValidationException e) {
            Assert.assertTrue(e.getFieldErrors().contains(DtoFieldCodes.FIELD_USER_INFO_UPDATE_DTO_USER_BIRTH_DATE));
        }
    }

    @Test
    public void testInvalidUserInfoUpdateServiceCall() throws SgDataValidationException {
        UserInfoUpdateDto dto = new UserInfoUpdateDto();
        dto.setUserBirthDate(INVALID_USER_BIRTH_DATE);

        try {
            service.setUserInfo(1L, dto);
            Assert.fail("Expected " + SgDataValidationException.class.getName());
        } catch (SgDataValidationException e) {
            Assert.assertEquals(new HashSet<String>(Arrays.asList(new String[]{
                DtoFieldCodes.FIELD_USER_INFO_UPDATE_DTO_USER_BIRTH_DATE,})),
                    e.getFieldErrors());
        }
    }

    @Test
    public void testInvalidCompleteSCompleteignupDtoPassword() throws SgDataValidationException {
        testInvalidResetPasswordDtoPasswordImpl(INVALID_PASSWORD_NULL);
        testInvalidResetPasswordDtoPasswordImpl(INVALID_PASSWORD_1);
        testInvalidResetPasswordDtoPasswordImpl(INVALID_PASSWORD_2);
    }

    private void testInvalidResetPasswordDtoPasswordImpl(String password) {
        ResetPasswordDto dto = new ResetPasswordDto();
        dto.setPassword(password);
        try {
            validatorComponent.validate(dto);
            Assert.fail("Expected " + SgDataValidationException.class.getName());
        } catch (SgDataValidationException e) {
            Assert.assertTrue(e.getFieldErrors().contains(DtoFieldCodes.FIELD_RESET_PASSWORD_DTO_PASSWORD));
        }
    }

    @Test
    public void testValidResetPasswordDtoPassword() throws SgDataValidationException {
        testValidResetPasswordDtoPasswordImpl(VALID_PASSWORD_1);
        testValidResetPasswordDtoPasswordImpl(VALID_PASSWORD_2);
    }

    private void testValidResetPasswordDtoPasswordImpl(String password) throws SgDataValidationException {
        ResetPasswordDto dto = new ResetPasswordDto();
        dto.setPassword(password);
        validatorComponent.validate(dto);
    }

    @Test
    public void testInvalidResetPasswordServiceCall() throws SgDataValidationException {
        ResetPasswordDto dto = new ResetPasswordDto();
        dto.setPassword(INVALID_PASSWORD_1);

        try {
            service.resetPassword(1L, dto);
            Assert.fail("Expected " + SgDataValidationException.class.getName());
        } catch (SgDataValidationException e) {
            Assert.assertEquals(new HashSet<String>(Arrays.asList(new String[]{
                DtoFieldCodes.FIELD_RESET_PASSWORD_DTO_PASSWORD,})),
                    e.getFieldErrors());
        }
    }

}
