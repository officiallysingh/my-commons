package com.clearcaptions.ccwsv3.common.boot.config.provisioning;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.bandwidth.BandwidthClient;
import com.bandwidth.iris.sdk.IrisClient;

import feign.auth.BasicAuthRequestInterceptor;

@Configuration
@ConditionalOnProperty(prefix = "application.provisioning.bandwidth", name = "base-url")
@EnableConfigurationProperties(BandwidthProperties.class)
public class BandwidthAutoConfiguration {
	
	@Configuration
	@ConditionalOnMissingBean(value = BandwidthClient.class)
	@ConditionalOnClass(value = { BandwidthClient.class })
	public static class BandwidthClientConfiguration {
		
		@ConditionalOnMissingBean(value = BandwidthClient.class)
		@Bean
		public BandwidthClient bandwidthClient(final BandwidthProperties properties) {
			BandwidthClient client = new BandwidthClient.Builder()
//		            .messagingBasicAuthCredentials("MESSAGING_API_TOKEN", "MESSAGING_API_SECRET")
//		            .voiceBasicAuthCredentials("VOICE_API_USERNAME", "VOICE_API_PASSWORD")
		            .twoFactorAuthBasicAuthCredentials(properties.getUsername(), properties.getPassword())
		            .environment(com.bandwidth.Environment.CUSTOM) // Optional - sets the enviroment to a custom base URL
		            .baseUrl(properties.getBaseUrl()) // Optional - sets the base Url
		            .build();
			
			return client;
		}
	}

	@Configuration
	@ConditionalOnMissingBean(value = IrisClient.class)
	@ConditionalOnClass(value = { IrisClient.class })
	public static class IrisClientConfiguration {
		
		private static final String IRIS_CLIENT_API_VERSION = "v1.0";
		
		@ConditionalOnMissingBean(value = IrisClient.class)
		@Bean
		public IrisClient irisClient(final BandwidthProperties properties) {
			return new IrisClient(properties.getBaseUrl(),
					properties.getAccountId(), properties.getUsername(), 
					properties.getPassword(), IRIS_CLIENT_API_VERSION);
		}
	}

	@Configuration
	@EnableConfigurationProperties(value = { BandwidthProperties.class })
	@ConditionalOnProperty(prefix = "application.provisioning.bandwidth", name = "base-url")
	@ConditionalOnClass(FeignClient.class)
	public static class BandwidthFeignClientAuthConfiguration {

		@Bean
		public BasicAuthRequestInterceptor bandwidthBasicAuthRequestInterceptor(
				final BandwidthProperties properties) {
			return new BasicAuthRequestInterceptor(properties.getUsername(), properties.getPassword());
		}
	}
}
