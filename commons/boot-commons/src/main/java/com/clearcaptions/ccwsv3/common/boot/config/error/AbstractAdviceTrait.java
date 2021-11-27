package com.clearcaptions.ccwsv3.common.boot.config.error;

import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.context.request.NativeWebRequest;
import org.zalando.problem.Problem;
import org.zalando.problem.spring.web.advice.AdviceTrait;

/**
 * @author Rajveer Singh
 */
public abstract class AbstractAdviceTrait implements AdviceTrait {

	/**
	 * Post-process Problem payload to add the message key for front-end if needed,
	 * currently doing nothing
	 */
	@Override
	public ResponseEntity<Problem> process(@Nullable ResponseEntity<Problem> entity, final NativeWebRequest request) {
		// if (entity == null || entity.getBody() == null) {
		// return entity;
		// }
		// else {
		// return entity;
		// }
		// Do nothing
		return entity;
	}

}
