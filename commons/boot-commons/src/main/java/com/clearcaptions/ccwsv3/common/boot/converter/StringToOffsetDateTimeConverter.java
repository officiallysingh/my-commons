package com.clearcaptions.ccwsv3.common.boot.converter;

import java.time.OffsetDateTime;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

@ReadingConverter
public enum StringToOffsetDateTimeConverter implements Converter<String, OffsetDateTime> {
    INSTANCE;

    @Override
    public OffsetDateTime convert(String source) {
        if(source == null){
            return null;
        } else{
        	return OffsetDateTime.parse(source);
        }
    }
}
