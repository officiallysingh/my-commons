package com.clearcaptions.ccwsv3.common.boot.config.swagger.customizer;

import static com.google.common.collect.Lists.newArrayList;

import java.util.List;

import org.springframework.core.Ordered;
import org.springframework.data.domain.Pageable;

import com.clearcaptions.ccwsv3.common.boot.config.pagination.PaginationProperties;
import com.fasterxml.classmate.ResolvedType;
import com.fasterxml.classmate.TypeResolver;

import springfox.documentation.builders.RequestParameterBuilder;
import springfox.documentation.schema.CollectionType;
import springfox.documentation.schema.ScalarType;
import springfox.documentation.schema.TypeNameExtractor;
import springfox.documentation.service.ParameterStyle;
import springfox.documentation.service.ParameterType;
import springfox.documentation.service.RequestParameter;
import springfox.documentation.service.ResolvedMethodParameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.OperationBuilderPlugin;
import springfox.documentation.spi.service.contexts.OperationContext;
import springfox.documentation.spi.service.contexts.ParameterContext;
import springfox.documentation.swagger.common.SwaggerPluginSupport;

/**
 * The Springfox Plugin to resolve {@link Pageable} parameter into plain fields.
 *
 * @author Rajveer Singh
 */
public class PageableParameterBuilderPlugin implements OperationBuilderPlugin, Ordered {

	private final PaginationProperties properties;

	private final TypeNameExtractor nameExtractor;

	private final TypeResolver resolver;

	private final ResolvedType pageableType;

	public PageableParameterBuilderPlugin(PaginationProperties properties, TypeNameExtractor nameExtractor, TypeResolver resolver) {
		this.properties = properties;
		this.nameExtractor = nameExtractor;
		this.resolver = resolver;
		this.pageableType = resolver.resolve(Pageable.class);
	}

	@Override
	public boolean supports(DocumentationType delimiter) {
		return SwaggerPluginSupport.pluginDoesApply(delimiter);
	}

	@Override
	public int getOrder() {
		return SwaggerPluginSupport.SWAGGER_PLUGIN_ORDER + 30;
	}
	
	@Override
	public void apply(OperationContext context) {
		List<RequestParameter> parameters = newArrayList();
		int i = 0;
		for (ResolvedMethodParameter methodParameter : context.getParameters()) {
			ResolvedType resolvedType = methodParameter.getParameterType();
			if (this.pageableType.equals(resolvedType)) {
				ParameterContext parameterContext = new ParameterContext(methodParameter,
						context.getDocumentationContext(), context.getGenericsNamingStrategy(), context, i++);

				parameters.add(createPageParameter(parameterContext));
				parameters.add(createSizeParameter(parameterContext));
				parameters.add(createSortParameter(parameterContext));

				context.operationBuilder().requestParameters(parameters);
			}
		}
	}

	/**
	 * Create a page parameter. Override it if needed. Set a default value for example.
	 * @param context {@link Pageable} parameter context
	 * @return The page parameter
	 */
	protected RequestParameter createPageParameter(ParameterContext context) {
		return new RequestParameterBuilder().name(this.properties.getPageParamName()).in(ParameterType.QUERY)
				.query(q -> q.defaultValue("" + this.properties.defaultPageNumber()).style(ParameterStyle.SIMPLE)
	            .model(m -> m.scalarModel(ScalarType.INTEGER)))
				.description(this.properties.getPageParamDescription()).build();
	}

	/**
	 * Create a size parameter. Override it if needed. Set a default value for example.
	 * @param context {@link Pageable} parameter context
	 * @return The size parameter
	 */
	protected RequestParameter createSizeParameter(ParameterContext context) {
		return new RequestParameterBuilder().name(this.properties.getSizeParamName()).in(ParameterType.QUERY)
				.query(q -> q.defaultValue("" + this.properties.getDefaultPageSize()).style(ParameterStyle.SIMPLE)
	            .model(m -> m.scalarModel(ScalarType.INTEGER)))
				.description(this.properties.getSizeParamDescription()).build();
	}

	protected RequestParameter createSortParameter(ParameterContext context) {
		return new RequestParameterBuilder().name(this.properties.getSortParamName()).in(ParameterType.QUERY)
				.query(q -> q.allowEmptyValue(true).style(ParameterStyle.SIMPLE)
	            .model(m -> m.collectionModel(cm -> cm.collectionType(CollectionType.LIST)
	            		.model(cms -> cms.scalarModel(ScalarType.STRING)))))
				.example(null).description(this.properties.getSortParamDescription()).build();
	}

	TypeResolver getResolver() {
		return this.resolver;
	}

	TypeNameExtractor getNameExtractor() {
		return this.nameExtractor;
	}
}
