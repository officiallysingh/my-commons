package com.clearcaptions.ccwsv3.common.boot.config.error;

import java.io.Serializable;

import org.springframework.validation.FieldError;

import com.clearcaptions.ccwsv3.common.error.resolver.FieldErrorMessageResolver;
import com.clearcaptions.ccwsv3.common.message.MessageProvider;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;

/**
 * @author Rajveer Singh
 */
@Getter
public class FieldErrorVM implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@JsonInclude(value = Include.NON_NULL)
	private final String objectName;

	private final String field;

	private final String message;

	@JsonInclude(value = Include.NON_NULL)
	private final Object rejectedValue;

	@JsonInclude(value = Include.NON_NULL)
	private final String messageCode;

	@JsonInclude(value = Include.NON_NULL)
	private final String[] messageCodes;

	private FieldErrorVM(ProblemProperties properties, String objectName, String field, String message,
			Object rejectedValue, String messageCode, String[] messageCodes) {
		this.objectName = properties.isDebugInfo() ? objectName : null;
		this.field = field;
		this.message = message;
		this.rejectedValue = properties.isDebugInfo() ? rejectedValue : null;
		this.messageCode = properties.isDebugInfo() ? messageCode : null;
		this.messageCodes = properties.isDebugInfo() ? messageCodes : null;
	}

	public static FieldErrorVM of(ProblemProperties properties, final FieldError fieldError) {
		return new FieldErrorVM(properties, fieldError.getObjectName(), fieldError.getField(),
				MessageProvider.getMessage(FieldErrorMessageResolver.of(fieldError)),
				fieldError.getRejectedValue(), fieldError.getCode(), fieldError.getCodes());
	}

}
