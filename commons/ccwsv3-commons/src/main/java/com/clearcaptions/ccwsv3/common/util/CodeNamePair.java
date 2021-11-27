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
 * @param <T> Code
 * @param <S> Name
 */
@Getter
@AllArgsConstructor(staticName = "of")
@ToString
@EqualsAndHashCode(of = { "code" })
@ApiModel(value = "codeNamePair", description = "Code and Name pair")
public class CodeNamePair<T, S> implements PairItem<T, S> {

    @NonNull
    @ApiModelProperty(name = "code", dataType = "String", value = "Code", example = "XYD112")
    private T code;

    @NonNull
    @ApiModelProperty(name = "name", dataType = "String", value = "Name", example = "Foo Bar")
    private S name;

    @Override
    public CodeNamePair<T, S> pair() {
        return this;
    }

    @SuppressWarnings("unchecked")
    @Override
    public int compareTo(PairItem<T, S> other) {
        return ((Comparable<S>)this.name).compareTo(other.pair().getName());
    }
}
