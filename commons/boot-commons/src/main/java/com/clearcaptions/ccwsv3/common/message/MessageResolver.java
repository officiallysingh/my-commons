package com.clearcaptions.ccwsv3.common.message;

/**
 * @author Rajveer Singh
 */
public interface MessageResolver {

	String ERROR_MESSAGE_CODE_PREFIX = "error";

	String TITLE_MESSAGE_CODE_PREFIX = "error.title";

	public String messageCode();

	public default String defaultMessage() {
		return "Message not found for code: '" + this.messageCode() + "'";
	}

}
