package com.clearcaptions.ccwsv3.common.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @author Rajveer Singh
 */
public interface DecimalHelper {

	public static final int DECIMAL_POINTS_8 = 8;

	public static final int DECIMAL_POINTS_3 = 3;

	public static final BigDecimal ZERO = BigDecimal.ZERO;

	public static final BigDecimal ONE = BigDecimal.ONE;

	public static BigDecimal preciseTo8Decimals(final BigDecimal bigDecimal) {
		return preciseToDecimals(bigDecimal, DECIMAL_POINTS_8);
	}

	public static BigDecimal preciseToDecimals(final BigDecimal bigDecimal, int scale) {
		if (bigDecimal == null) {
			return null;
		}
		return bigDecimal.setScale(scale, RoundingMode.HALF_UP);
	}

	public static BigDecimal preciseTo3Decimals(final BigDecimal bigDecimal) {
		return preciseTo3Decimals(bigDecimal, DECIMAL_POINTS_3);
	}

	public static BigDecimal preciseTo3Decimals(final BigDecimal bigDecimal, int scale) {
		if (bigDecimal == null) {
			return null;
		}
		return bigDecimal.setScale(scale, RoundingMode.HALF_UP);
	}

	public static boolean isNegative(final BigDecimal value) {
		return value.compareTo(ZERO) < 0;
	}

	public static boolean isPositive(final BigDecimal value) {
		return value.compareTo(ZERO) > 0;
	}

	public static boolean isZero(final BigDecimal value) {
		return value.compareTo(ZERO) == 0;
	}

	public static boolean isOne(final BigDecimal value) {
		return value.compareTo(ONE) == 0;
	}

	public static boolean equals(final BigDecimal value1, final BigDecimal value2) {
		return value1.compareTo(value2) == 0;
	}

	public static BigDecimal of(final double value) {
		return new BigDecimal(value);
	}

	public static void validatePositive(final BigDecimal value, final String valueType) {
		if (isNegative(value)) {
			// TODO: Throw appropriate exception
			// throw new
			// RateException(RateExceptionExceptionType.VALUE_CAN_NOT_BE_NEGATIVE,
			// Status.BAD_REQUEST, valueType,
			// value);
		}
	}

}
