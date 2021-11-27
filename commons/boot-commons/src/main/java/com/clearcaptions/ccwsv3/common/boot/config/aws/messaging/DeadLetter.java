package com.clearcaptions.ccwsv3.common.boot.config.aws.messaging;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor(staticName = "of")
public class DeadLetter<T> {

	private T payload;

	private String failureReason;
}
