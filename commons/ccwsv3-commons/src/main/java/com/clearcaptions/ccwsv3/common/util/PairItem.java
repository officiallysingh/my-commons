package com.clearcaptions.ccwsv3.common.util;

/**
 * @author Rajveer Singh
 * @param <T> Pair's first element type
 * @param <S> Pair's second element type
 */
public interface PairItem<T, S> extends Comparable<PairItem<T, S>> {

    CodeNamePair<T, S> pair(); 
}
