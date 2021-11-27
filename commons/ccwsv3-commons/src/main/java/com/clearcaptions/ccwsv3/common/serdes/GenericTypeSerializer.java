package com.clearcaptions.ccwsv3.common.serdes;

import java.io.IOException;
import java.math.BigDecimal;

import com.clearcaptions.ccwsv3.common.util.DecimalHelper;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

/**
 * @author Rajveer Singh
 */
public class GenericTypeSerializer extends JsonSerializer<Object> {

    @Override
    public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers)
            throws IOException, JsonProcessingException {
        if (value == null) {
            gen.writeString("");
        }
        else if (value instanceof String && ((String) value) != null) {
            gen.writeString(value.toString());
        }
        else if (value instanceof BigDecimal) {
            gen.writeString(String.format("%." + DecimalHelper.DECIMAL_POINTS_8 + "f", value));
        }
    }
}