package com.clearcaptions.ccwsv3.common.boot.config.error;

/**
 * @author Rajveer Singh
 */
public interface ProblemConstants {

	String ERROR_DECRIPTION_FILE_URI = "/errors";

	String ERROR_STATIC_RESOURCES_PATH = "classpath:/static/problems/";

	String ERROR_DECRIPTION_FILE_NAME = "problem.html";

	String ERROR_DECRIPTION_FILE = ERROR_DECRIPTION_FILE_URI + "/" + ERROR_DECRIPTION_FILE_NAME;

	public static interface Keys {

		String MESSAGE = "message";

		String LOCALIZED_MESSAGE = "localizedMessage";

		String TIMESTAMP = "timestamp";

		String VIOLATIONS = "violations";

		String FIELD_ERRORS = "fieldErrors";

		String STACKTRACE = "stacktrace";

	}

}
