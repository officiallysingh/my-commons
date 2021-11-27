package com.clearcaptions.ccwsv3.common.boot.config.swagger.customizer;

import springfox.documentation.spring.web.plugins.Docket;

/**
 * Callback interface that can be implemented by beans wishing to further customize the
 * {@link Docket} in Springfox.
 *
 * @author Rajveer Singh
 */
@FunctionalInterface
public interface SwaggerCustomizer {

	public enum Order {

		ONE(1), TWO(2), THREE(3), FOUR(4), FIVE(5);

		private final int value;

		private Order(int value) {
			this.value = value;
		}

		public int value() {
			return this.value;
		}

	}

	/**
	 * Customize the Springfox Docket.
	 * @param docket the Docket to customize
	 */
	void customize(Docket docket);

}
