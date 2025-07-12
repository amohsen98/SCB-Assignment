package com.scb.application.constants;


public class RegexConstants {

    public static final String NAME_PATTERN = "^[a-zA-Z]+(([',. -][a-zA-Z ])?[a-zA-Z]*)*$";


    public static final String DATE_PATTERN = "^\\d{4}-\\d{2}-\\d{2}$";

    private RegexConstants() {
        throw new IllegalStateException("Constants class");
    }
}