package com.clearcaptions.ccwsv3.common.boot.config.error;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Rajveer Singh
 */
@Getter
@Setter
@NoArgsConstructor
@ToString
@ConditionalOnProperty(prefix = "application.problem", name = "enabled", havingValue = "true")
@ConfigurationProperties(prefix = "application.problem")
public class ProblemProperties {

	private boolean enabled = false;

	private boolean debugInfo = false;

}
