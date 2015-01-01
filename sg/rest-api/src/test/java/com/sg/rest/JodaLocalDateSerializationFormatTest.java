/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sg.rest;

import com.sg.domain.spring.configuration.JacksonMapperContext;
import com.sg.dto.request.SignupDto;
import com.sg.dto.request.UserInfoUpdateDto;
import com.sg.dto.serialization.DateTimeFormatStrings;
import java.io.IOException;
import org.codehaus.jackson.map.ObjectMapper;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
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
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = {JacksonMapperContext.class})
public class JodaLocalDateSerializationFormatTest {

    @Autowired
    ObjectMapper jacksonObjectMapper;
    
    private static final LocalDate DATE = LocalDate.parse("2014-01-28");
    
    @Test
    public void test() throws IOException {
        UserInfoUpdateDto dto = new UserInfoUpdateDto();
        dto.setUserBirthDate(DATE);
        String s = jacksonObjectMapper.writeValueAsString(dto);
        
        String sExpectedDate = DateTimeFormat.forPattern(DateTimeFormatStrings.DATE_FORMAT).print(DATE);
        
        Assert.assertTrue(s.contains(sExpectedDate));
        
        UserInfoUpdateDto read = jacksonObjectMapper.readValue(s, UserInfoUpdateDto.class);
        
        Assert.assertEquals(dto, read);
    }
    
}
