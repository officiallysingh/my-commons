package com.clearcaptions.ccwsv3.common.error;

import org.zalando.problem.Exceptional;
import org.zalando.problem.ThrowableProblem;

import com.clearcaptions.ccwsv3.common.error.resolver.ErrorResolver;

/**
 * Checked type Problem if needed
 * @author Rajveer Singh
 */
public class ApplicationException extends Exception implements Exceptional {

    private static final long serialVersionUID = 1L;
    
    private Exception cause;
    
    private ErrorResolver errorResolver;
    
    private ApplicationException(
            final String message,
            final Exception cause) {
    	super(message, cause);
    }
    
    private ApplicationException(
            final String message) {
    	super(message);
    }
    
    public ApplicationException(final ErrorResolver resolver) {
        this(resolver.message());
        this.errorResolver = resolver;
    }

    public ApplicationException(final ErrorResolver resolver, final Exception cause) {
        this(resolver.message(), cause);
        this.errorResolver = resolver;
        this.cause = cause;
    }
    
	public ErrorResolver getErrorResolver() {
		return this.errorResolver;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.zalando.problem.Exceptional#getCause()
	 */
	@Override
	public ThrowableProblem getCause() {
		return this.cause == null ? null : ProblemUtil.toProblem(this.cause, this.errorResolver.status());
	}

}
