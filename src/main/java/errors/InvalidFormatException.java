package errors;

/**
 * Created by Sougata on 4/30/2016.
 */
public class InvalidFormatException extends Exception {

    //Parameterless Constructor
    public InvalidFormatException() {
    }

    //Constructor that accepts a message
    public InvalidFormatException(final String message) {
        super(message);
    }
}
