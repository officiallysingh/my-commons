package com.clearcaptions.ccwsv3.common.boot.config.provisioning;

import javax.validation.Valid;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
@ConditionalOnProperty(prefix = "application.provisioning.bandwidth", name = "base-url")
@ConfigurationProperties(prefix = "application.provisioning.bandwidth")
@Valid
public class BandwidthProperties {

	private String baseUrl;

	private String accountId;

	private String username;

	private String password;
}
