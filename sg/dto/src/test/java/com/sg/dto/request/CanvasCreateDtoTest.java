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
public class CanvasCreateDtoTest extends BaseDtoConstraintsTest {
    
    @Test
    public void testDto()
    {
        CanvasCreateDto dto = new CanvasCreateDto();
        Set<String> constraintMessages = new HashSet<String>();
        constraintMessages.add(CanvasCreateDto.CODE_FIELD);
        constraintMessages.add(CanvasCreateDto.SIZE_FIELD);
        testConstraintViolations(dto, constraintMessages);
    }
}
