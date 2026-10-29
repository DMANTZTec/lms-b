package com.dmantz.lms.entity;

public enum ProgramDuration {
    ONE_MONTH("1 Month"),
    TWO_MONTHS("2 Months"),
    THREE_MONTHS("3 Months"),
    FOUR_MONTHS("4 Months"),
    FIVE_MONTHS("5 Months"),
    SIX_MONTHS("6 Months"),
    SEVEN_MONTHS("7 Months"),
    EIGHT_MONTHS("8 Months"),
    NINE_MONTHS("9 Months"),
    TEN_MONTHS("10 Months"),
    ELEVEN_MONTHS("11 Months"),
    TWELVE_MONTHS("12 Months");

    private final String label;

    ProgramDuration(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}