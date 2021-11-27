package com.clearcaptions.ccwsv3.common.rest.response;

import static com.clearcaptions.ccwsv3.common.message.GeneralMessageResolver.NOT_FOUND;
import static com.clearcaptions.ccwsv3.common.message.MessageProvider.getMessage;

import java.net.URI;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;

import com.clearcaptions.ccwsv3.common.boot.config.DefaultBeanRegistry;
import com.clearcaptions.ccwsv3.common.boot.config.pagination.PaginatedResource;
import com.clearcaptions.ccwsv3.common.boot.config.pagination.PaginatedResourceAssembler;
import com.clearcaptions.ccwsv3.common.rest.response.builder.AbstractResponseBuilder;
import com.clearcaptions.ccwsv3.common.rest.response.builder.MessageBuilder;
import com.clearcaptions.ccwsv3.common.rest.response.builder.StatusBuilder;

/**
 * @author Rajveer Singh
 */
public class ResponseEntityHelper {

	public static <T> StatusBuilder<T, ResponseEntity<T>> of(final T body) {
		Assert.notNull(body, "Body must not be null");
		return new ResponseEntityBuilder<T>(body);
	}
	
	public static <T> MessageBuilder<T, ResponseEntity<T>> of(final Optional<T> body) {
		Assert.notNull(body, "Body must not be null");
		return body.isPresent() ? new ResponseEntityBuilder<T>(body.get(), HttpStatus.OK) : 
			new ResponseEntityBuilder<T>(HttpStatus.NOT_FOUND);
	}
	
	public static <T> MessageBuilder<T, ResponseEntity<T>> accepted() {
		return new ResponseEntityBuilder<T>(HttpStatus.ACCEPTED);
	}

	public static <T> MessageBuilder<T, ResponseEntity<T>> badRequest() {
		return new ResponseEntityBuilder<T>(HttpStatus.BAD_REQUEST);
	}

	public static <T> MessageBuilder<T, ResponseEntity<T>> noContent() {
		return new ResponseEntityBuilder<T>(HttpStatus.NO_CONTENT);
	}

	public static <T> MessageBuilder<T, ResponseEntity<T>> notFound() {
		return new ResponseEntityBuilder<T>(HttpStatus.NOT_FOUND);
	}

	public static <T> MessageBuilder<T, ResponseEntity<T>> created(final URI location) {
		return new ResponseEntityBuilder<T>(location);
	}

	public static <T> MessageBuilder<T, ResponseEntity<T>> created(final URI location, final T body) {
		return new ResponseEntityBuilder<T>(location, body);
	}

	public static <T> MessageBuilder<T, ResponseEntity<T>> ok(final T body) {
		return new ResponseEntityBuilder<T>(body, HttpStatus.OK);
	}

	public static <T> MessageBuilder<T, ResponseEntity<T>> ok() {
		return new ResponseEntityBuilder<T>(HttpStatus.OK);
	}
	
	public static <T> MessageBuilder<Page<T>, ResponseEntity<PaginatedResource<T>>> page(final Page<T> content) {
		return new ResponseEntityPageBuilder<T>(content);
	}

	public static <T> MessageBuilder<Page<T>, ResponseEntity<PaginatedResource<T>>> page(final Page<T> content, final URI requestUri) {
		return new ResponseEntityPageBuilder<T>(content, requestUri);
	}

	public static <T> MessageBuilder<Page<T>, ResponseEntity<PaginatedResource<T>>> emptyPage(final HttpStatus status) {
		return new ResponseEntityPageBuilder<T>(status);
	}

	public static class ResponseEntityBuilder<T> extends AbstractResponseBuilder<T, ResponseEntity<T>> {

		private URI location;
		
		ResponseEntityBuilder(final HttpStatus status) {
			super(status);
		}

		ResponseEntityBuilder(final T body) {
			super(body);
		}

		ResponseEntityBuilder(final T body, final HttpStatus status) {
			super(body, status);
		}

		ResponseEntityBuilder(final URI location) {
			super(HttpStatus.CREATED);
			Assert.notNull(location, "location must not be null");
			this.location = location;
		}

		ResponseEntityBuilder(final URI location, final T body) {
			super(body, HttpStatus.CREATED);
			Assert.notNull(location, "location must not be null");
			Assert.notNull(body, "body must not be null");
			this.location = location;
		}

		@Override
		public ResponseEntity<T> build() {
			org.springframework.http.ResponseEntity.BodyBuilder bodyBuilder = ResponseEntity.status(this.status);
			if(this.location != null) {
				bodyBuilder.location(this.location);
			}
			if(this.headers != null) {
				bodyBuilder.headers(this.headers);
			}
			return this.body != null ? bodyBuilder.body(this.body) : bodyBuilder.build();
		}
	}

	public static class ResponseEntityPageBuilder<T> extends AbstractResponseBuilder<Page<T>, ResponseEntity<PaginatedResource<T>>> {
		
		private URI requestUri;

		ResponseEntityPageBuilder(final HttpStatus status) {
			super(status);
		}

		ResponseEntityPageBuilder(final Page<T> body) {
			super(body, body.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK);
		}

		ResponseEntityPageBuilder(final Page<T> body, final URI requestUri) {
			super(body, body.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK);
			Assert.notNull(requestUri, "requestUri must not be null");
			this.requestUri = requestUri;
		}

		ResponseEntityPageBuilder(final Page<T> body, final HttpStatus status) {
			super(body, status);
		}

		ResponseEntityPageBuilder(final Page<T> body, final HttpStatus status, final URI requestUri) {
			super(body, status);
			Assert.notNull(requestUri, "requestUri must not be null");
			this.requestUri = requestUri;
		}

		@Override
		public ResponseEntity<PaginatedResource<T>> build() {
			PaginatedResourceAssembler pageAssembler = DefaultBeanRegistry.getPageAssembler();
			org.springframework.http.ResponseEntity.BodyBuilder bodyBuilder = ResponseEntity.status(this.status);
			if(this.body.isEmpty()) {
				super.addHeader(this.status == HttpStatus.NOT_FOUND ? ERROR : INFO, getMessage(NOT_FOUND));
			}
			if(this.headers != null) {
				bodyBuilder.headers(this.headers);
			}
			PaginatedResource<T> pageBody = this.requestUri == null 
					? this.body != null ? pageAssembler.assemble(this.body) : null
							: this.body != null ? pageAssembler.assemble(this.body, this.requestUri) : null;
			return pageBody != null ? bodyBuilder.body(pageBody) : bodyBuilder.build();
		}
	}
	
}
