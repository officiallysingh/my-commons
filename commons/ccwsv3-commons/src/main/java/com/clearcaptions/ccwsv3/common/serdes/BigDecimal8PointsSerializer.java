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
public class BigDecimal8PointsSerializer extends JsonSerializer<BigDecimal> {

    @Override
    public void serialize(BigDecimal value, JsonGenerator jgen, SerializerProvider provider)
            throws IOException, JsonProcessingException {
        jgen.writeString(String.format("%." + DecimalHelper.DECIMAL_POINTS_8 + "f", value));
    }
}
