package com.clearcaptions.ccwsv3.common.boot.config.swagger.customizer;

import org.springframework.core.Ordered;
import org.springframework.data.domain.Pageable;

import springfox.documentation.spring.web.plugins.Docket;

/**
 * A swagger customizer to setup {@link Docket} settings.
 *
 * @author Rajveer Singh
 */
public class SwaggerPaginationCustomizer implements SwaggerCustomizer, Ordered {

	private int order = Order.THREE.value();

	@Override
	public void customize(Docket docket) {
		docket.ignoredParameterTypes(Pageable.class);
	}

	public void setOrder(int order) {
		this.order = order;
	}

	@Override
	public int getOrder() {
		return this.order;
	}

}
