/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sg.dto.serialization;

import java.io.IOException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;

/**
 *
 * @author tarasev
 */
//Default org.codehaus.jackson implementations
//@JsonDeserialize(using = LocalDateDeserializer.class)
//@JsonSerialize(using = LocalDateSerializer.class)
public class JodaLocalDateJsonSerializer extends JsonSerializer<LocalDate> {

    public JodaLocalDateJsonSerializer() {}
    
    @Override
    public void serialize(LocalDate date, JsonGenerator gen, SerializerProvider provider) throws IOException, JsonProcessingException {
        String formattedDate = DateTimeFormat.forPattern(DateTimeFormatStrings.DATE_FORMAT).print(date);

        gen.writeString(formattedDate);
    }
    
}
