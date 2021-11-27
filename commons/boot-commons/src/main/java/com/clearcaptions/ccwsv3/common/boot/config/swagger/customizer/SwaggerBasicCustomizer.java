package com.clearcaptions.ccwsv3.common.boot.config.swagger.customizer;

import static springfox.documentation.schema.AlternateTypeRules.newRule;

import java.nio.ByteBuffer;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.core.Ordered;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.async.DeferredResult;
import org.zalando.problem.Problem;

import com.clearcaptions.ccwsv3.common.boot.config.swagger.SwaggerProperties;
import com.clearcaptions.ccwsv3.common.error.ProblemResponse;
import com.fasterxml.classmate.TypeResolver;

import springfox.documentation.schema.AlternateTypeRule;
import springfox.documentation.schema.WildcardType;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * A swagger customizer to setup {@link Docket} settings.
 *
 * @author Rajveer Singh
 */
public class SwaggerBasicCustomizer implements SwaggerCustomizer, Ordered {

	private int order = Order.ONE.value();

	private final SwaggerProperties swaggerProperties;

	private final TypeResolver typeResolver;

	private final Optional<AlternateTypeRule[]> alternateTypeRules;

	public SwaggerBasicCustomizer(final SwaggerProperties swaggerProperties, final TypeResolver typeResolver,
			final ObjectProvider<AlternateTypeRule[]> alternateTypeRules) {
		this.swaggerProperties = swaggerProperties;
		this.typeResolver = typeResolver;
		this.alternateTypeRules = Optional.ofNullable(alternateTypeRules.getIfAvailable());
	}

	@Override
	public void customize(Docket docket) {

		Contact contact = new Contact(this.swaggerProperties.getContactName(), this.swaggerProperties.getContactUrl(),
				this.swaggerProperties.getContactEmail());

		ApiInfo apiInfo = new ApiInfo(this.swaggerProperties.getTitle(), this.swaggerProperties.getDescription(),
				this.swaggerProperties.getVersion(), this.swaggerProperties.getTermsOfServiceUrl(), contact,
				this.swaggerProperties.getLicense(), this.swaggerProperties.getLicenseUrl(), new ArrayList<>());

		docket.host(this.swaggerProperties.getHost())
				.protocols(new HashSet<>(Arrays.asList(this.swaggerProperties.getProtocols()))).apiInfo(apiInfo)
				.useDefaultResponseMessages(this.swaggerProperties.isUseDefaultResponseMessages())
				.forCodeGeneration(true).directModelSubstitute(LocalDate.class, String.class)
				.directModelSubstitute(ByteBuffer.class, String.class)
				.directModelSubstitute(Problem.class, ProblemResponse.class)
				.genericModelSubstitutes(ResponseEntity.class, List.class, Set.class)
				.alternateTypeRules(newRule(
						this.typeResolver.resolve(DeferredResult.class,
								this.typeResolver.resolve(ResponseEntity.class, WildcardType.class)),
						this.typeResolver.resolve(WildcardType.class)));

		this.alternateTypeRules.ifPresent(docket::alternateTypeRules);
	}

	public void setOrder(int order) {
		this.order = order;
	}

	@Override
	public int getOrder() {
		return this.order;
	}

}
