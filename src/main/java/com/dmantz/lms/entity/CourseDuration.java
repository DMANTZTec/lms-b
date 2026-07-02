package com.dmantz.lms.entity;

	public enum CourseDuration {
	    TWO_WEEKS("2 Weeks"),
	    FOUR_WEEKS("4 Weeks"),
	    SIX_WEEKS("6 Weeks"),
	    EIGHT_WEEKS("8 Weeks"),
	    TEN_WEEKS("10 Weeks"),
	    TWELVE_WEEKS("12 Weeks"),
	    FOURTEEN_WEEKS("14 Weeks"),
	    SIXTEEN_WEEKS("16 Weeks"),
	    EIGHTEEN_WEEKS("18 Weeks"),
	    TWENTY_WEEKS("20 Weeks");

	    private final String label;

	    CourseDuration(String label) {
	        this.label = label;
	    }

	    public String getLabel() {
	        return label;
	    }
	}

