package com.clearcaptions.ccwsv3.common;

import org.springframework.context.support.AbstractApplicationContext;

/**
 * Application constants.
 */
public interface BootConstant {

//	String API_CONTEXT = "/**";
//
//	String SYSTEM_USER = "SYSTEM";
//
//	ZoneId ZONE_ID_UTC = ZoneId.of("UTC");
//
//	ZoneId ZONE_ID_IST = ZoneId.of("Asia/Kolkata");
//
//	TimeZone TIME_ZONE_UTC = TimeZone.getTimeZone(ZONE_ID_UTC);
//
//	TimeZone TIME_ZONE_IST = TimeZone.getTimeZone(ZONE_ID_IST);
//
//	List<ZoneId> SUPPORTED_TIME_ZONES = Arrays.asList(ZONE_ID_UTC, ZONE_ID_IST);
//
//	Locale DEFAULT_LOCALE = Locale.getDefault();
//
//	ZoneId DEFAULT_ZONE_ID = ZoneId.systemDefault();
//
//	TimeZone DEFAULT_TIME_ZONE = TIME_ZONE_UTC;
//	
//	String HEADER_AUTHORIZATION = "Authorization";

	public static interface BeanName {

		String APPLICATION_TASK_EXECUTOR_BEAN_NAME = "applicationTaskExecutor";

		String SCHEDULED_TASK_EXECUTOR_BEAN_NAME = "scheduledTaskExecutor";

		String PROPERTY_SOURCES_PLACEHOLDER_CONFIGURER_BEAN_NAME = "propertySourcesPlaceholderConfigurer";

		String CONFIG_MANAGER_BEAN_NAME = "configManager";

		String PAGINATED_RESOURCE_ASSEMBLER_BEAN_NAME = "pageAssembler";

		String APPLICATION_EVENT_MULTICASTER_BEAN_NAME = AbstractApplicationContext.APPLICATION_EVENT_MULTICASTER_BEAN_NAME;

		String SECURITY_CONFIGURATION_BEAN_NAME = "securityConfiguration";

		String APPLICATION_EXCEPTION_HANDLER_BEAN_NAME = "applicationExceptionHandler";

		String SECURITY_EXCEPTION_HANDLER_BEAN_NAME = "securityExceptionHandler";

		String WEB_EXCEPTION_HANDLER_BEAN_NAME = "webExceptionHandler";

		String SWAGGER_AUTO_CONFIGURATION_BEAN_NAME = "swaggerAutoConfiguration";

		String PROBLEM_AUTO_CONFIGURATION_BEAN_NAME = "problemAutoConfiguration";

		String APPLICATION_API_DOCKET_BEAN_NAME = "defaultApi";

		String ACTUATOR_API_DOCKET_BEAN_NAME = "actuatorApi";

		String WEB_CONFIGURER_BEAN_NAME = "webConfigurer";
		
		String COUCHBASE_CONFIGURATION_BEAN_NAME = "couchbaseConfiguration";
		
		String AMAZON_DYNAMODB_BEAN_NAME = "amazonDynamoDB";

	}

}
