import errors.InvalidFormatException;
import fileParser.FileParser;
import fileParser.FileParserImpl;

/**
 * Created by Sougata on 4/29/2016.
 */
public class Main {

    public static void main(String[] args) throws InvalidFormatException {
        FileParser fileParser = new FileParserImpl();
        System.out.println("fileParser.validateBookingRequestFormat(\"2015-98-17 10:17:06\") = "
                + fileParser.validateMeetingScheduleFormat(null));

    }
}



