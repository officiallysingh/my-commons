package com.clearcaptions.ccwsv3.common.boot.config.aws.messaging;

import static io.awspring.cloud.core.config.AmazonWebserviceClientConfigurationUtils.GLOBAL_CLIENT_CONFIGURATION_BEAN_NAME;

import java.util.Collections;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.handler.annotation.support.PayloadMethodArgumentResolver;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSAsync;
import com.amazonaws.services.sns.AmazonSNSAsyncClientBuilder;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.awspring.cloud.autoconfigure.messaging.SnsProperties;
import io.awspring.cloud.context.annotation.ConditionalOnMissingAmazonClient;
import io.awspring.cloud.core.region.RegionProvider;
import io.awspring.cloud.core.region.StaticRegionProvider;
import io.awspring.cloud.messaging.config.QueueMessageHandlerFactory;
import io.awspring.cloud.messaging.core.NotificationMessagingTemplate;
import io.awspring.cloud.messaging.core.QueueMessagingTemplate;

@Configuration
public class MessagingAutoConfiguration {

	@Configuration
	@ConditionalOnClass(AmazonSQS.class)
	@ConditionalOnProperty(prefix = "cloud.aws.sqs", name = "enabled", havingValue = "true")
	static class sqsConfiguration {

		@Bean
		public QueueMessagingTemplate queueMessagingTemplate(final AmazonSQSAsync amazonSQSAsync) {
			return new QueueMessagingTemplate(amazonSQSAsync);
		}

		@Bean
		public SqsClient sqsClient(final QueueMessagingTemplate queueMessagingTemplate) {
			return new SqsClient(queueMessagingTemplate);
		}

	}

	@Configuration
	@ConditionalOnClass(AmazonSNS.class)
	@ConditionalOnProperty(prefix = "cloud.aws.sns", name = "enabled", havingValue = "true")
	class SnsConfiguration {

		private final AWSCredentialsProvider awsCredentialsProvider;

		private final RegionProvider regionProvider;

		private final ClientConfiguration clientConfiguration;

		SnsConfiguration(ObjectProvider<AWSCredentialsProvider> awsCredentialsProvider,
				ObjectProvider<RegionProvider> regionProvider, SnsProperties properties,
				@Qualifier(GLOBAL_CLIENT_CONFIGURATION_BEAN_NAME) ObjectProvider<ClientConfiguration> globalClientConfiguration,
				@Qualifier("snsClientConfiguration") ObjectProvider<ClientConfiguration> snsClientConfiguration) {
			this.awsCredentialsProvider = awsCredentialsProvider.getIfAvailable();
			this.regionProvider = properties.getRegion() == null ? regionProvider.getIfAvailable()
					: new StaticRegionProvider(properties.getRegion());
			this.clientConfiguration = snsClientConfiguration.getIfAvailable(globalClientConfiguration::getIfAvailable);
		}

		@ConditionalOnMissingAmazonClient(AmazonSNS.class)
		@Bean
		public AmazonSNSAsync amazonSNS() {
			AmazonSNSAsyncClientBuilder amazonSNSAsyncClientBuilder = AmazonSNSAsyncClientBuilder.standard()
					.withCredentials(this.awsCredentialsProvider).withRegion(this.regionProvider.getRegion().getName())
					.withClientConfiguration(this.clientConfiguration)
					.withRequestHandlers(new SnsFifoMessageGroupIdHandler());
			return amazonSNSAsyncClientBuilder.build();
		}

		@Bean
		public NotificationMessagingTemplate notificationMessagingTemplate(final AmazonSNS amazonSns) {
			return new NotificationMessagingTemplate(amazonSns);
		}

		@Bean
		public SnsClient snsClient(final NotificationMessagingTemplate notificationMessagingTemplate) {
			return new SnsClient(notificationMessagingTemplate);
		}

	}

	@Configuration
	@ConditionalOnClass(QueueMessageHandlerFactory.class)
	@ConditionalOnProperty(prefix = "cloud.aws.sqs", name = "enabled", havingValue = "true")
	class QueueMessageHandlerFactoryConfiguration {

		@Bean
		public QueueMessageHandlerFactory queueMessageHandlerFactory(final AmazonSQSAsync amazonSQS,
				final ObjectMapper objectMapper) {
			QueueMessageHandlerFactory factory = new QueueMessageHandlerFactory();
			factory.setAmazonSqs(amazonSQS);
			factory.setArgumentResolvers(
					Collections.singletonList(new PayloadMethodArgumentResolver(mappingJackson2MessageConverter(objectMapper))));
			return factory;
		}

		private MappingJackson2MessageConverter mappingJackson2MessageConverter(final ObjectMapper objectMapper) {
			MappingJackson2MessageConverter messageConverter = new MappingJackson2MessageConverter();
			messageConverter.setStrictContentTypeMatch(false);
			messageConverter.setObjectMapper(objectMapper);
			return messageConverter;
		}

	}

}
