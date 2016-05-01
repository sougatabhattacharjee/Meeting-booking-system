import errors.InvalidFormatException;
import fileParser.FileParser;
import fileParser.FileParserImpl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

/**
 * Created by Sougata on 4/29/2016.
 */
public class Main {

    public static void main(String[] args) throws InvalidFormatException, IOException {
        final String inputFileName = "input";
        final MeetingScheduler meetingScheduler = new MeetingScheduler();
        meetingScheduler.process(inputFileName);
    }

}



