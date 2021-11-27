package com.clearcaptions.ccwsv3.common.serdes;

import java.io.IOException;
import java.time.LocalTime;

import com.clearcaptions.ccwsv3.common.util.DateTimeHelper;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

/**
 * @author Rajveer Singh
 */
public class LocalTimeSerializer extends JsonSerializer<LocalTime> {

    @Override
    public void serialize(LocalTime value, JsonGenerator jgen, SerializerProvider provider)
            throws IOException, JsonProcessingException {
        jgen.writeString(DateTimeHelper.formatTime(value));
    }
}
