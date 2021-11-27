package com.clearcaptions.ccwsv3.common.boot.config.swagger;

import static com.clearcaptions.ccwsv3.common.BootConstant.BeanName.SWAGGER_AUTO_CONFIGURATION_BEAN_NAME;

import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.clearcaptions.ccwsv3.common.boot.config.security.SecurityProperties;

import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.boot.starter.autoconfigure.OpenApiAutoConfiguration;
import springfox.boot.starter.autoconfigure.SwaggerUiWebFluxConfiguration;
import springfox.documentation.swagger2.configuration.Swagger2DocumentationConfiguration;

/**
 * Springfox Swagger configuration.
 * <p>
 * Warning! When having a lot of REST endpoints, Springfox can become a performance issue.
 * In that case, you can skip swagger by not including swagger Spring profile in dev/prod
 * application yml, so that this bean is ignored.
 *
 * @author Rajveer Singh
 */
@Configuration(value = SWAGGER_AUTO_CONFIGURATION_BEAN_NAME)
@EnableConfigurationProperties(value = { SwaggerProperties.class, SecurityProperties.class })
@AutoConfigureBefore(OpenApiAutoConfiguration.class)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
@ConditionalOnProperty(prefix = "application.swagger", name = "enabled", havingValue = "true")
@ConditionalOnMissingBean(name = SWAGGER_AUTO_CONFIGURATION_BEAN_NAME)
@Import({ BeanValidatorPluginsConfiguration.class, Swagger2DocumentationConfiguration.class,
		SwaggerUiWebFluxConfiguration.class, SwaggerConfiguration.class })
public class ReactriveSwaggerAutoConfiguration {

}
