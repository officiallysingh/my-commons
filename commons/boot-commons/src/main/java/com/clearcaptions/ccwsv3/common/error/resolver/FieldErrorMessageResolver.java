package com.clearcaptions.ccwsv3.common.error.resolver;

import org.springframework.context.MessageSourceResolvable;
import org.springframework.validation.FieldError;

import lombok.RequiredArgsConstructor;

/**
 * @author Rajveer Singh
 */
@RequiredArgsConstructor(staticName = "of")
public class FieldErrorMessageResolver implements MessageSourceResolvable {

	private final FieldError fieldError;
	
	@Override
	public String[] getCodes() {
		return this.fieldError.getCodes();
	}
	
	@Override
	public Object[] getArguments() {
		return this.fieldError.getArguments();
	}

	@Override
	public String getDefaultMessage() {
		return this.fieldError.getDefaultMessage();
	}
}
