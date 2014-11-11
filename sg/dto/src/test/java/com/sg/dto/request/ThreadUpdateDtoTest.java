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
public class ThreadUpdateDtoTest extends BaseDtoConstraintsTest {

    @Test
    public void testDto()
    {
        ThreadUpdateDto dto = new ThreadUpdateDto();
        Set<String> constraintMessages = new HashSet<String>();
        constraintMessages.add(ThreadUpdateDto.CODE_FIELD);
        constraintMessages.add(ThreadUpdateDto.REF_CODE_FIELD);
        testConstraintViolations(dto, constraintMessages);
    }
}
