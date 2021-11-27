package com.clearcaptions.ccwsv3.common.util;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

/**
 * @author Rajveer Singh
 * @param <T> Code type
 * @param <S> Name Type
 * @param <U> Type type
 */
@Getter
@AllArgsConstructor(staticName = "of")
@ToString
@EqualsAndHashCode(of = { "code" })
@ApiModel(value = "triplet", description = "Code, Name and Type triplet")
public class Triple<T, S, U> {

    @NonNull
    @ApiModelProperty(name = "code", dataType = "String", value = "Code", example = "XYD112")
    private T code;

    @NonNull
    @ApiModelProperty(name = "name", dataType = "String", value = "Name", example = "Foo Bar")
    private S name;

    @NonNull
    @ApiModelProperty(name = "type", dataType = "String", value = "Type", example = "AGENT")
    private U type;
}
