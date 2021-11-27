package com.clearcaptions.ccwsv3.common.boot.config.error;

import java.io.Serializable;

import javax.validation.ConstraintViolation;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;

/**
 * @author Rajveer Singh
 */
@Getter
//@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ViolationVM implements Serializable {

	private static final long serialVersionUID = 1L;

	@JsonInclude(value = Include.NON_NULL)
	private final String objectName;

	private final String field;

	private final String message;

	@JsonInclude(value = Include.NON_NULL)
	private final String messageCode;

	@JsonInclude(value = Include.NON_NULL)
	private final Object rejectedValue;

	private ViolationVM(ProblemProperties properties, String objectName, String field, String message, String messageCode, Object rejectedValue) {
		this.objectName = properties.isDebugInfo() ? objectName : null;
		this.field = field;
		this.message = message;
		this.messageCode = messageCode.replace("{", "").replace("}", "");
		this.rejectedValue = properties.isDebugInfo() ? rejectedValue : null;
	}

	public static ViolationVM of(ProblemProperties properties, ConstraintViolation<?> voilation) {
		return new ViolationVM(properties, voilation.getRootBeanClass().getName(),
				voilation.getPropertyPath().toString(), voilation.getMessage(),
				voilation.getMessageTemplate(), voilation.getInvalidValue());
	}

}
