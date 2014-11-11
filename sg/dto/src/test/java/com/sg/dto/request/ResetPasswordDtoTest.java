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
public class ResetPasswordDtoTest extends BaseDtoConstraintsTest {

    @Test
    public void testDto()
    {
        ResetPasswordDto dto = new ResetPasswordDto();
        Set<String> constraintMessages = new HashSet<String>();
        constraintMessages.add(ResetPasswordDto.PASSWORD_FIELD);
        testConstraintViolations(dto, constraintMessages);
    }
}
