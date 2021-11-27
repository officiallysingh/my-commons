package com.clearcaptions.ccwsv3.common.boot.config.pagination;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.data.domain.PageRequest;

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
@ConditionalOnProperty(prefix = "application.pagination", name = "enabled", havingValue = "true")
@ConfigurationProperties(prefix = "application.pagination")
@Valid
public class PaginationProperties {

	/**
	 * Whether or not to enable Pagination
	 */
	private boolean enabled = false;

	private boolean firstAndLastRels = false;

	@Positive
	private int defaultPageSize = 16;

	@Positive
	private int maxPageSize = 2000;
	
	@NotEmpty
	private String pageParamName = "page";

	@NotEmpty
	private String pageParamDescription = "Requested Page number. zero-based page index, must not be negative";

	@NotEmpty
	private String sizeParamName = "size";

	@NotEmpty
	private String sizeParamDescription = "Size of a page. Allowed between 1 and %d, must not be negative";

	@NotEmpty
	private String sortParamName = "sort";

	@NotEmpty
	private String sortParamDescription = "Sorting criteria in the format: property(,asc|desc). "
			+ "Default sort order is ascending. Multiple sort criteria are supported.";
	
	public String getPageParamDescription() {
		return String.format(this.pageParamDescription);
	}

	public String getSizeParamDescription() {
		return String.format(this.sizeParamDescription, this.maxPageSize);
	}
	
	public int defaultPageNumber() {
		return 0;
	}

	public PageRequest defaultPage() {
		return PageRequest.of(0, this.defaultPageSize);
	}

}
