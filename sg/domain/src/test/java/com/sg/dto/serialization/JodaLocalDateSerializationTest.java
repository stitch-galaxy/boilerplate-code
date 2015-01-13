/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.dto.serialization;

import java.io.IOException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author Администратор
 */
public class JodaLocalDateSerializationTest {

    ObjectMapper jacksonObjectMapper = new ObjectMapper();

    private static final LocalDate DATE = LocalDate.parse("2014-01-28");

    private static class TestClass {

        @JsonSerialize(using = JodaLocalDateJsonSerializer.class)
        @JsonDeserialize(using = JodaLocalDateJsonDeserializer.class)
        private LocalDate date;

        /**
         * @return the date
         */
        public LocalDate getDate() {
            return date;
        }

        /**
         * @param date the date to set
         */
        public void setDate(LocalDate date) {
            this.date = date;
        }

    }

    @Test
    public void testSerialization() throws IOException {
        TestClass toSerialize = new TestClass();
        toSerialize.setDate(DATE);
        String s = jacksonObjectMapper.writeValueAsString(toSerialize);

        String sExpectedDate = DateTimeFormat.forPattern(DateTimeFormatStrings.DATE_FORMAT).print(DATE);

        Assert.assertTrue(s.contains(sExpectedDate));
        TestClass deserialized = jacksonObjectMapper.readValue(s, TestClass.class);

        Assert.assertEquals(toSerialize.getDate(), deserialized.getDate());
    }
}
