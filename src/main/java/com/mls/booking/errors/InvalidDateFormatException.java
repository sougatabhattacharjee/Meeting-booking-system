package com.mls.booking.errors;

/**
 * Created by Sougata on 4/30/2016.
 */
public class InvalidDateFormatException extends InvalidFormatException {

    //Parameterless Constructor
    public InvalidDateFormatException() {
    }

    //Constructor that accepts a message
    public InvalidDateFormatException(final String message) {
        super(message);
    }

}
