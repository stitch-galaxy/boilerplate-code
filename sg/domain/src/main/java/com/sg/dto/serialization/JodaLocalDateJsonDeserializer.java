/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sg.dto.serialization;

import java.io.IOException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;

/**
 *
 * @author tarasev
 */
public class JodaLocalDateJsonDeserializer extends JsonDeserializer<LocalDate> {

    public JodaLocalDateJsonDeserializer() {}
    
    @Override
    public LocalDate deserialize(JsonParser parser, DeserializationContext context) throws IOException, JsonProcessingException {
        return DateTimeFormat.forPattern(DateTimeFormatStrings.DATE_FORMAT).parseLocalDate(parser.getText());
    }
    
}
