package errors;

/**
 * Created by Sougata on 4/30/2016.
 */
public class InvalidFileFormatException extends Exception {

    //Parameterless Constructor
    public InvalidFileFormatException() {}

    //Constructor that accepts a message
    public InvalidFileFormatException(final String message)
    {
        super(message);
    }
}
