package com.sg.domain.test.jpa;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import com.sg.domain.service.SgService;
import com.sg.domain.service.ValidatorComponent;
import com.sg.domain.service.exception.SgDataValidationException;
import com.sg.domain.spring.configuration.JpaContext;
import com.sg.domain.spring.configuration.JpaServiceContext;
import com.sg.domain.spring.configuration.MapperContext;
import com.sg.domain.spring.configuration.ValidatorContext;
import com.sg.dto.request.CompleteSignupDto;
import com.sg.dto.request.SigninDto;
import com.sg.dto.request.SignupDto;
import com.sg.dto.request.ThreadDeleteDto;
import com.sg.dto.request.ThreadDto;
import com.sg.dto.request.ThreadUpdateDto;
import java.util.Arrays;
import java.util.HashSet;
import javax.annotation.Resource;
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
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = {ValidatorContext.class, JpaContext.class, MapperContext.class, JpaServiceContext.class})
public class JpaServiceDataValidationTest {

    @Autowired
    private SgService service;

    @Resource
    private ValidatorComponent validatorComponent;

    private static final String VALID_USER_LAST_NAME = "cY";
    private static final String VALID_USER_FIRST_NAME = "eZy";
    private static final LocalDate VALID_USER_BIRTH_DATE = new LocalDate(2014, 01, 01);
    private static final String VALID_EMAIL = "eTarasov@rencap.com ";
    
    private static final String INVALID_USER_LAST_NAME = "";
    private static final String INVALID_USER_FIRST_NAME = "";
    private static final LocalDate INVALID_USER_BIRTH_DATE = null;
    private static final String INVALID_EMAIL = " abc ";
    
    private static final String VALID_PASSWORD = "1хороший пароль";
    private static final String INVALID_PASSWORD = "badpassword";
    
    private static final String INVALID_THREAD_CODE = "";
    private static final String VALID_THREAD_CODE = "DMC";

    @Test
    public void testInvalidSignupUserDto() {
        SignupDto dto = new SignupDto();
        dto.setEmail(INVALID_EMAIL);
        dto.setUserBirthDate(INVALID_USER_BIRTH_DATE);
        dto.setUserFirstName(INVALID_USER_FIRST_NAME);
        dto.setUserLastName(INVALID_USER_LAST_NAME);
        
        try {
            service.signupUser(dto);
            Assert.fail("Expected " + SgDataValidationException.class.getName());
        } catch (SgDataValidationException e) {
            Assert.assertEquals(new HashSet<String>(Arrays.asList(new String[]{
                SignupDto.FIELD_SIGNUP_EMAIL,
                SignupDto.FIELD_SIGNUP_USER_BIRTH_DATE,
                SignupDto.FIELD_SIGNUP_USER_FIRST_NAME,
                SignupDto.FIELD_SIGNUP_USER_LAST_NAME,})),
                    e.getFieldErrors());
        }
    }

    @Test
    public void testInvalidSignupAdminDto() {
        SignupDto dto = new SignupDto();
        dto.setEmail(INVALID_EMAIL);
        dto.setUserBirthDate(INVALID_USER_BIRTH_DATE);
        dto.setUserFirstName(INVALID_USER_FIRST_NAME);
        dto.setUserLastName(INVALID_USER_LAST_NAME);
        try {
            service.signupAdmin(dto);
            Assert.fail("Expected " + SgDataValidationException.class.getName());
        } catch (SgDataValidationException e) {
            Assert.assertEquals(new HashSet<String>(Arrays.asList(new String[]{
                SignupDto.FIELD_SIGNUP_EMAIL,
                SignupDto.FIELD_SIGNUP_USER_BIRTH_DATE,
                SignupDto.FIELD_SIGNUP_USER_FIRST_NAME,
                SignupDto.FIELD_SIGNUP_USER_LAST_NAME,})),
                    e.getFieldErrors());
        }
    }

    @Test
    public void testValidSignupDto() throws SgDataValidationException {
        SignupDto dto = new SignupDto();
        dto.setEmail(VALID_EMAIL);
        dto.setUserBirthDate(VALID_USER_BIRTH_DATE);
        dto.setUserFirstName(VALID_USER_FIRST_NAME);
        dto.setUserLastName(VALID_USER_LAST_NAME);
        validatorComponent.validate(dto);
    }

    @Test
    public void testInvalidCompleteSignupDto() {
        CompleteSignupDto dto = new CompleteSignupDto();
        dto.setPassword(INVALID_PASSWORD);
        try {
            service.completeSignup(1L, dto);
            Assert.fail("Expected " + SgDataValidationException.class.getName());
        } catch (SgDataValidationException e) {
            Assert.assertEquals(new HashSet<String>(Arrays.asList(new String[]{
                CompleteSignupDto.FIELD_COMPLETE_SIGNUP_PASSWORD
            })),
                    e.getFieldErrors());
        }
    }

    @Test
    public void testValidCompleteSignupDto() throws SgDataValidationException {
        CompleteSignupDto dto = new CompleteSignupDto();
        dto.setPassword(VALID_PASSWORD);
        validatorComponent.validate(dto);
    }

    @Test
    public void testInvalidSigninDto() {
        SigninDto dto = new SigninDto();
        dto.setPassword(INVALID_PASSWORD);
        dto.setEmail(INVALID_EMAIL);
        try {
            service.signIn(dto);
            Assert.fail("Expected " + SgDataValidationException.class.getName());
        } catch (SgDataValidationException e) {
            Assert.assertEquals(new HashSet<String>(Arrays.asList(new String[]{
                SigninDto.FIELD_SIGNIN_EMAIL,
                SigninDto.FIELD_SIGNIN_PASSWORD,})),
                    e.getFieldErrors());
        }
    }

    @Test
    public void testValidSigninDto() throws SgDataValidationException {
        SigninDto dto = new SigninDto();
        dto.setPassword(VALID_PASSWORD);
        dto.setEmail(VALID_EMAIL);
        validatorComponent.validate(dto);
    }
    
    @Test
    public void testInvalidThreadDto() {
        ThreadDto dto = new ThreadDto();
        dto.setCode(INVALID_THREAD_CODE);
        try {
            service.create(dto);
            Assert.fail("Expected " + SgDataValidationException.class.getName());
        } catch (SgDataValidationException e) {
            Assert.assertEquals(new HashSet<String>(Arrays.asList(new String[]{
                ThreadDto.FIELD_THREAD_CODE,
            })),
                    e.getFieldErrors());
        }
    }
    

    @Test
    public void testValidThreadDto() throws SgDataValidationException {
        ThreadDto dto = new ThreadDto();
        dto.setCode(VALID_THREAD_CODE);
        validatorComponent.validate(dto);
    }
    
    @Test
    public void testInvalidThreadDeleteDto() {
        ThreadDeleteDto dto = new ThreadDeleteDto();
        dto.setCode(INVALID_THREAD_CODE);
        try {
            service.delete(dto);
            Assert.fail("Expected " + SgDataValidationException.class.getName());
        } catch (SgDataValidationException e) {
            Assert.assertEquals(new HashSet<String>(Arrays.asList(new String[]{
                ThreadDeleteDto.FIELD_THREAD_CODE,
            })),
                    e.getFieldErrors());
        }
    }

    @Test
    public void testValidThreadDeleteDto() throws SgDataValidationException {
        ThreadDeleteDto dto = new ThreadDeleteDto();
        dto.setCode(VALID_THREAD_CODE);
        validatorComponent.validate(dto);
    }
    
    @Test
    public void testInvalidThreadUpdateDto() {
        ThreadUpdateDto dto = new ThreadUpdateDto();
        dto.setCode(INVALID_THREAD_CODE);
        dto.setRefCode(INVALID_THREAD_CODE);
        try {
            service.update(dto);
            Assert.fail("Expected " + SgDataValidationException.class.getName());
        } catch (SgDataValidationException e) {
            Assert.assertEquals(new HashSet<String>(Arrays.asList(new String[]{
                ThreadUpdateDto.FIELD_THREAD_CODE,
                ThreadUpdateDto.FIELD_THREAD_REF_CODE,
            })),
                    e.getFieldErrors());
        }
    }

    @Test
    public void testValidThreadUpdateDto() throws SgDataValidationException {
        ThreadUpdateDto dto = new ThreadUpdateDto();
        dto.setCode(VALID_THREAD_CODE);
        dto.setRefCode(VALID_THREAD_CODE);
        validatorComponent.validate(dto);
    }

}
