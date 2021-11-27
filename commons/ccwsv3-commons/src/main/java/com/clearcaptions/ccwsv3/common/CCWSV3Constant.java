package com.clearcaptions.ccwsv3.common;

import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Application common constants.
 */
public interface CCWSV3Constant {

	String SLASH = "/";

	String API = SLASH;

	String VERSION_v1 = "v1";

	String VERSION_v2 = "v2";

	String VERSION_v3 = "v3";

	String CURRENT_VERSION = VERSION_v3;

	String CCWSV3_API = API + VERSION_v3;

	String API_CONTEXT = "/**";

	String HEADER_AUTHORIZATION = "Authorization";

	String HEADER_CORRELATION_ID = "X-Correlation-Id";

	String HEADER_USER_ID = "X-User-Id";
	
	String HEADER_NOTIFY_URL = "X-Notify-Url";
	
	String HEADER_NOTIFY_METHOD = "X-Notify-Method";

	String E164_PHONE_NUMBER_REGEX = "^\\+?[1-9]\\d{1,14}$";

	String AREA_CODE_REGEX = "^[0-9]{3}$";
	
	String ZIP_CODE_REGEX = "^[0-9]{5}(?:-[0-9]{4})?$";
	
	String SPEL_RANDOM_UUID = "#{ T(java.util.UUID).randomUUID() }";
	
	String SYSTEM_USER = "SYSTEM";

	ZoneId ZONE_ID_UTC = ZoneId.of("UTC");

	ZoneId ZONE_ID_IST = ZoneId.of("Asia/Kolkata");

	TimeZone TIME_ZONE_UTC = TimeZone.getTimeZone(ZONE_ID_UTC);

	TimeZone TIME_ZONE_IST = TimeZone.getTimeZone(ZONE_ID_IST);

	List<ZoneId> SUPPORTED_TIME_ZONES = Arrays.asList(ZONE_ID_UTC, ZONE_ID_IST);

	Locale DEFAULT_LOCALE = Locale.getDefault();

	ZoneId DEFAULT_ZONE_ID = ZoneId.systemDefault();

	TimeZone DEFAULT_TIME_ZONE = TIME_ZONE_UTC;

}
