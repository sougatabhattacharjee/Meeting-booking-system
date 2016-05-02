package com.mls.booking;

import com.mls.booking.errors.InvalidFormatException;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.Scanner;

import static com.mls.booking.fileParser.FileParserParams.DEFAULT_INPUT_FILE;
import static com.mls.booking.util.Helpers.isStringEmpty;


/**
 * Created by Sougata on 4/29/2016.
 */
public class Main {

    private final static Logger LOGGER = Logger.getLogger(Main.class);

    public static void main(String[] args) throws InvalidFormatException, IOException {
        LOGGER.info("Application Started!");

        String inputFileName = "";

        final Scanner one = new Scanner(System.in);
        System.out.println("Enter File Name : ");
        inputFileName = one.next();

        final MeetingScheduler meetingScheduler = new MeetingScheduler();
        meetingScheduler.process(isStringEmpty(inputFileName) ? DEFAULT_INPUT_FILE : inputFileName);
    }
}



