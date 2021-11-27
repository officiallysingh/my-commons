package com.clearcaptions.ccwsv3.common.model;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
public class PhoneNumber {

	private String countryCode;
	
	private String subscriber;
	
	public static String getSubscriber(final String phoneNumber) {
		Assert.isTrue(StringUtils.isNotEmpty(phoneNumber), "phoneNumber must not be null or empty!");
		return phoneNumber.startsWith("+1") ? phoneNumber.substring(2).trim() : phoneNumber.trim();
	}
}
