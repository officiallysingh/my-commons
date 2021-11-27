package com.clearcaptions.ccwsv3.common.error;

import java.time.OffsetDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author Rajveer Singh
 */
// Just a dummy class to be used to display example value in swagger
@ApiModel(value = "errorDetails", description = "Response of an error occurred on server side")
public abstract class ProblemResponse {

	@JsonProperty
	@ApiModelProperty(value = "Title of error such as Remittance Transaction exception etc.", required = true,
			example = "Error category", position = 1)
	private String title;

	@JsonProperty
	@ApiModelProperty(value = "HTTP Status code sach as 200 or 400 etc.", required = true, example = "400",
			position = 2)
	private int status;

	@JsonProperty
	@ApiModelProperty(value = "The rest API path where the error has occurred such as /api/v1/prices", required = true,
			example = "/api/v1/prices", position = 3)
	private String instance;

	@JsonProperty
	@ApiModelProperty(value = "The text message in EN Locale to be displayed to user specifying the cause of error",
			required = true, example = "Invalid currency code: NUMR", position = 4)
	private String message;

	@JsonProperty
	@ApiModelProperty(value = "The localized text message to be displayed to user specifying the cause of error",
			required = true, example = "Invalid currency code: NUMR", position = 5)
	private String localizedMessage;

	@JsonProperty
	@ApiModelProperty(value = "The timestamp when the error has occurred", required = true,
			example = "Invalid currency code: NUMR", position = 6)
	private OffsetDateTime timestamp;

}
