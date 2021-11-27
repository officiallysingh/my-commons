package com.clearcaptions.ccwsv3.common.boot.converter;

import java.time.OffsetDateTime;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;

@WritingConverter
public enum OffsetDateTimeToStringConverter implements Converter<OffsetDateTime, String> {
    INSTANCE;

    @Override
    public String convert(OffsetDateTime source) {
        if(source == null){
            return null;
        } else{
            return source.toString();
        }
    }
}
