/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.dto.request;

import java.util.HashSet;
import java.util.Set;
import org.junit.Test;

/**
 *
 * @author tarasev
 */
public class SignupDtoTest extends BaseDtoConstraintsTest {

    @Test
    public void testDto()
    {
        SignupDto dto = new SignupDto();
        dto.setUserFirstName(EMPTY_STRING);
        dto.setUserLastName(EMPTY_STRING);
        Set<String> constraintMessages = new HashSet<String>();
        constraintMessages.add(SignupDto.EMAIL_FIELD);
        constraintMessages.add(SignupDto.USER_FIRSTNAME_FIELD);
        constraintMessages.add(SignupDto.USER_LASTNAME_FIELD);
        testConstraintViolations(dto, constraintMessages);
    }
}
