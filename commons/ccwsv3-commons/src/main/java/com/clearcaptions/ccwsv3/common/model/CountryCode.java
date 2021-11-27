package com.clearcaptions.ccwsv3.common.model;

public enum CountryCode {

	US("United States"), CA("Canada");
	
	private final String name;
	
	private CountryCode(final String name) {
        this.name = name;
    }
	
	public static CountryCode fromName(final String name) {
		for(CountryCode country: CountryCode.values()) {
			if(country.getName().equalsIgnoreCase(name))
				return country;
		}
		throw new IllegalArgumentException("Invalid Country name:" + name);
	}
	
	public String getName() {
		return this.name;
	}
}
