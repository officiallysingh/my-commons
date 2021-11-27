package com.clearcaptions.ccwsv3.common.error.resolver;

import org.zalando.problem.Status;
import org.zalando.problem.StatusType;

import com.clearcaptions.ccwsv3.common.message.MessageResolver;

/**
 * @author Rajveer Singh
 */
public interface ErrorResolver {

	String TYPE_INTERNAL_SERVER_ERROR = "internal-server-error";

	String TYPE_CONSTRAINT_VIOLATIONS = "constraint-violations";

	String TYPE_DATA_INTEGRITY_VIOLATION = "data-integrity-violation";

	String TYPE_CONCURRENCY_FAILURE = "concurrency-failure";

	String TYPE_VALIDATION_FAILURE = "validation-failure";

	String TYPE_NOT_FOUND = "not-found";

	String TYPE_BAD_REQUEST = "bad-request";

	public String message();

	public String localizedMessage();

	public String typeCode();

	public default StatusType status() {
		return Status.INTERNAL_SERVER_ERROR;
	}

	public String title();

	public ErrorResolver parameters(final Object... params);

	public ErrorResolver status(final StatusType status);

	public ErrorResolver titleResolver(final MessageResolver titleResolver);

}
