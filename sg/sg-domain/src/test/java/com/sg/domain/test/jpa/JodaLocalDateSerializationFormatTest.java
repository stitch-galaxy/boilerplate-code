/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sg.domain.test.jpa;

import com.sg.dto.request.SignupDto;
import com.sg.dto.serialization.DateTimeFormatStrings;
import java.io.IOException;
import org.codehaus.jackson.map.ObjectMapper;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author tarasev
 */
public class JodaLocalDateSerializationFormatTest {

    private static final LocalDate DATE = LocalDate.parse("2014-01-28");
    
    @Test
    public void test() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        SignupDto dto = new SignupDto();
        dto.setUserBirthDate(DATE);
        String s = mapper.writeValueAsString(dto);
        
        String sExpectedDate = DateTimeFormat.forPattern(DateTimeFormatStrings.DATE_FORMAT).print(DATE);
        
        Assert.assertTrue(s.contains(sExpectedDate));
        
        SignupDto read = mapper.readValue(s, SignupDto.class);
        
        Assert.assertEquals(dto, read);
    }
    
}
