package com.clearcaptions.ccwsv3.common.boot.config.aws.messaging;

import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
class MessageWrapper {
	
	String payload;
	
	Map<String, Object> headers;
}
