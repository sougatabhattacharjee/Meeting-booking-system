package fileParser;

import errors.InvalidFormatException;
import org.junit.Assert;
import org.junit.Test;

import javax.annotation.Nonnull;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static util.Helpers.isListEmpty;
import static util.Validator.checkNull;

/**
 * Created by Sougata on 4/30/2016.
 */
public class TestFileParser {

    private final static String INPUT_FILE_PATH = "fileParser/";

    @Test
    public void testCompanyOfficeHoursFormat_WhenInputIsValid() throws IOException, InvalidFormatException {
        final String inputFileName = "validInputToCheckCompanyOfficeHoursFormat";

        final List<String> companyOfficeHours = fileParse(INPUT_FILE_PATH + inputFileName);
        final FileParser fileParser = new FileParserImpl();

        for (final String companyOfficeHour : companyOfficeHours) {
            Assert.assertTrue(fileParser.validateCompanyOfficeHoursFormat(companyOfficeHour));
        }
    }

    @Test(expected = InvalidFormatException.class)
    public void testCompanyOfficeHoursFormat_WhenInputIsNotValid() throws IOException, InvalidFormatException {
        final String inputFileName = "inValidInputToCheckCompanyOfficeHoursFormat";

        final List<String> companyOfficeHours = fileParse(INPUT_FILE_PATH + inputFileName);
        final FileParser fileParser = new FileParserImpl();

        for (final String companyOfficeHour : companyOfficeHours) {
            Assert.assertFalse(fileParser.validateCompanyOfficeHoursFormat(companyOfficeHour));
        }
    }

    @Test
    public void testBookingRequestFormat_WhenInputIsValid() throws IOException, InvalidFormatException {
        final String inputFileName = "validInputToCheckBookingRequestFormat";

        final List<String> companyOfficeHours = fileParse(INPUT_FILE_PATH + inputFileName);
        final FileParser fileParser = new FileParserImpl();

        for (final String companyOfficeHour : companyOfficeHours) {
            Assert.assertTrue(fileParser.validateBookingRequestFormat(companyOfficeHour));
        }
    }

    @Test(expected = InvalidFormatException.class)
    public void testBookingRequestFormat_WhenInputIsNotValid() throws IOException, InvalidFormatException {
        final String inputFileName = "inValidInputToCheckBookingRequestFormat";

        final List<String> companyOfficeHours = fileParse(INPUT_FILE_PATH + inputFileName);
        final FileParser fileParser = new FileParserImpl();

        for (final String companyOfficeHour : companyOfficeHours) {
            Assert.assertFalse(fileParser.validateBookingRequestFormat(companyOfficeHour));
        }
    }

    @Test
    public void testMeetingScheduleFormat_WhenInputIsValid() throws IOException, InvalidFormatException {
        final String inputFileName = "validInputToCheckMeetingScheduleFormat";

        final List<String> companyOfficeHours = fileParse(INPUT_FILE_PATH + inputFileName);
        final FileParser fileParser = new FileParserImpl();

        for (final String companyOfficeHour : companyOfficeHours) {
            Assert.assertTrue(fileParser.validateMeetingScheduleFormat(companyOfficeHour));
        }
    }

    @Test(expected = InvalidFormatException.class)
    public void testMeetingScheduleFormat_WhenInputIsNotValid() throws IOException, InvalidFormatException {
        final String inputFileName = "inValidInputToCheckMeetingScheduleFormat";

        final List<String> companyOfficeHours = fileParse(INPUT_FILE_PATH + inputFileName);
        final FileParser fileParser = new FileParserImpl();

        for (final String companyOfficeHour : companyOfficeHours) {
            Assert.assertFalse(fileParser.validateMeetingScheduleFormat(companyOfficeHour));
        }
    }


    /**
     * Helpers method
     */
    @Nonnull
    private List<String> fileParse(@Nonnull final String fileName) throws IOException {
        checkNull(fileName, "fileName");

        final List<String> inputs = new ArrayList<String>();

        //Get file from resources folder
        final ClassLoader classLoader = getClass().getClassLoader();
        File file = null;
        if (classLoader.getResource(fileName) != null)
            file = new File(classLoader.getResource(fileName).getFile());
        else
            throw new FileNotFoundException(fileName + " file not found.");

        final BufferedReader reader = new BufferedReader(new FileReader(file.getPath()));
        String line = "";
        while ((line = reader.readLine()) != null) {
            inputs.add(line);
        }

        return isListEmpty(inputs) ? Collections.<String>emptyList() : inputs;
    }
}
