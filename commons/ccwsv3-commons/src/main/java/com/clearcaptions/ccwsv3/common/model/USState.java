package com.clearcaptions.ccwsv3.common.model;

import java.util.EnumSet;
import java.util.List;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import com.clearcaptions.ccwsv3.common.util.ImmutableCollectors;

public enum USState {

	AL("Alabama"),
	AK("Alaska"),
	AZ("Arizona"),
	AR("Arkansas"),
	CA("California"),
	CO("Colorado"),
	CT("Connecticut"),
	DC("District of Columbia"),
	DE("Delaware"),
	FL("Florida"),
	GA("Georgia"),
	GU("Guam"),  
	HI("Hawaii"),
	ID("Idaho"),
	IL("Illinois"),
	IN("Indiana"),
	IA("Iowa"),
	KS("Kansas"),
	KY("Kentucky"),
	LA("Louisiana"),  
	ME("Maine"),
	MD("Maryland"),
	MA("Massachusetts"),
	MI("Michigan"),
	MN("Minnesota"),
	MS("Mississippi"),
	MO("Missouri"),
	MT("Montana"),
	NE("Nebraska"),
	NV("Nevada"),
	NH("New Hampshire"),
	NJ("New Jersey"),
	NM("New Mexico"),
	NY("New York"),
	NC("North Carolina"),
	ND("North Dakota"),
	OH("Ohio"),
	OK("Oklahoma"),
	OR("Oregon"),
	PA("Pennsylvania"),
	PR("Puerto Rico"),
	RI("Rhode Island"),
	SC("South Carolina"),
	SD("South Dakota"),
	TN("Tennessee"),
	TX("Texas"),
	UT("Utah"),
	VT("Vermont"),
	VA("Virginia"),
	VI("Virgin Islands"),	
	WA("Washington"),
	WV("West Virginia"),
	WI("Wisconsin"),
	WY("Wyoming");

    final String name;
    
    private static final EnumSet<USState> ALL_VALUES = EnumSet.allOf(USState.class);

    private USState(final String name) {
        this.name = name;
    }
    
    /**
     * The full, name of this state.
     */
    public String getName() {
        return this.name;
    }

    public Pair<String, String> toPair() {
        return ImmutablePair.of(this.name(), this.getName());
    }

    public static EnumSet<USState> all() {
        return ALL_VALUES;
    }

    public static List<Pair<String, String>> list() {
        return ALL_VALUES.stream().map(USState::toPair).collect(ImmutableCollectors.toImmutableList());
    }

}
