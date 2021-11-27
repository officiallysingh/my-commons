package com.clearcaptions.ccwsv3.common.boot.config.pagination;

import static com.clearcaptions.ccwsv3.common.BootConstant.BeanName.PAGINATED_RESOURCE_ASSEMBLER_BEAN_NAME;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.HateoasPageableHandlerMethodArgumentResolver;
import org.springframework.data.web.HateoasSortHandlerMethodArgumentResolver;
import org.springframework.data.web.ReactivePageableHandlerMethodArgumentResolver;
import org.springframework.data.web.ReactiveSortHandlerMethodArgumentResolver;
import org.springframework.lang.Nullable;

import com.clearcaptions.ccwsv3.common.boot.config.swagger.SwaggerAutoConfiguration;

/**
 * @author Rajveer Singh
 */
@Configuration
@EnableConfigurationProperties(PaginationProperties.class)
@ConditionalOnClass(Pageable.class)
@ConditionalOnProperty(prefix = "application.pagination", name = "enabled", havingValue = "true")
@ConditionalOnMissingBean(PaginationAutoConfiguration.class)
public class PaginationAutoConfiguration {

	@Configuration
	@EnableConfigurationProperties(PaginationProperties.class)
	@AutoConfigureAfter(SwaggerAutoConfiguration.class)
	public static class PageAssemblerConfiguration {

		private final PaginationProperties properties;

		public PageAssemblerConfiguration(PaginationProperties properties) {
			this.properties = properties;
		}

		@ConditionalOnMissingBean(name = PAGINATED_RESOURCE_ASSEMBLER_BEAN_NAME)
		@Bean(PAGINATED_RESOURCE_ASSEMBLER_BEAN_NAME)
		public PaginatedResourceAssembler pageAssembler(
				@Nullable HateoasPageableHandlerMethodArgumentResolver resolver) {
			return new PaginatedResourceAssembler(resolver, this.properties.isFirstAndLastRels());
		}

		@ConditionalOnClass(HateoasPageableHandlerMethodArgumentResolver.class)
		@ConditionalOnMissingBean(HateoasPageableHandlerMethodArgumentResolver.class)
		@Bean
		public HateoasPageableHandlerMethodArgumentResolver hateoasPageableHandlerMethodArgumentResolver() {
			HateoasSortHandlerMethodArgumentResolver DEFAULT_SORT_RESOLVER = new HateoasSortHandlerMethodArgumentResolver();
			DEFAULT_SORT_RESOLVER.setSortParameter(this.properties.getSortParamName());
			HateoasPageableHandlerMethodArgumentResolver resolver = new HateoasPageableHandlerMethodArgumentResolver(
					DEFAULT_SORT_RESOLVER);
			resolver.setPageParameterName(this.properties.getPageParamName());
			resolver.setSizeParameterName(this.properties.getSizeParamName());
			resolver.setMaxPageSize(this.properties.getMaxPageSize());
			resolver.setFallbackPageable(this.properties.defaultPage());
			return resolver;
		}
	}

	@Configuration
	@EnableConfigurationProperties(PaginationProperties.class)
	@ConditionalOnClass(Pageable.class)
	@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
	@AutoConfigureAfter(SwaggerAutoConfiguration.class)
	@ConditionalOnProperty(prefix = "application.pagination", name = "enabled", havingValue = "true")
	public static class ReactivePageableHandlerMethodArgumentResolverAutoConfiguration {

		private final PaginationProperties properties;

		public ReactivePageableHandlerMethodArgumentResolverAutoConfiguration(PaginationProperties properties) {
			this.properties = properties;
		}

		@ConditionalOnClass(ReactivePageableHandlerMethodArgumentResolver.class)
		@ConditionalOnMissingBean(ReactivePageableHandlerMethodArgumentResolver.class)
		@Bean
		public ReactivePageableHandlerMethodArgumentResolver pageableHandlerMethodArgumentResolver() {
			ReactiveSortHandlerMethodArgumentResolver DEFAULT_SORT_RESOLVER = new ReactiveSortHandlerMethodArgumentResolver();
			DEFAULT_SORT_RESOLVER.setSortParameter(this.properties.getSortParamName());
			ReactivePageableHandlerMethodArgumentResolver resolver = new ReactivePageableHandlerMethodArgumentResolver(
					DEFAULT_SORT_RESOLVER);
			resolver.setPageParameterName(this.properties.getPageParamName());
			resolver.setSizeParameterName(this.properties.getSizeParamName());
			resolver.setMaxPageSize(this.properties.getMaxPageSize());
			resolver.setFallbackPageable(this.properties.defaultPage());
			return resolver;
		}
	}
}
