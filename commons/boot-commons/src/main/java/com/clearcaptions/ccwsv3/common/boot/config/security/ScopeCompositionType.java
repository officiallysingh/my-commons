package com.clearcaptions.ccwsv3.common.boot.config.security;

public enum ScopeCompositionType {
	
	CONJUNCTION("and"), DISJUNCTION("or");
	
	private String operator;
	
	private ScopeCompositionType(final String operator) {
		this.operator = operator;
	}
	
	public String operator() {
		return this.operator;
	}
}
