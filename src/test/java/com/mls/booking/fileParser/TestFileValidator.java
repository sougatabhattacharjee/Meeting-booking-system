package com.mls.booking.fileParser;

import com.mls.booking.errors.InvalidFormatException;
import org.junit.Assert;
import org.junit.Test;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import static com.mls.booking.util.Validator.checkNull;

/**
 * Created by Sougata on 4/30/2016.
 */
public class TestFileValidator {

    @Test
    public void testInputFileValidation_WhenValidInputs() throws IOException, InvalidFormatException {
        final String inputFileName = "inputValid";

        final File inputFile = getFile(inputFileName);
        final FileValidator fileValidator = new FileValidatorImpl();
        Assert.assertTrue(fileValidator.validateFileFormat(inputFile));
    }

    @Test(expected = InvalidFormatException.class)
    public void testInputFileValidation_WhenInValidInputs() throws IOException, InvalidFormatException {
        final String inputFileName = "inputInValid";

        final File inputFile = getFile(inputFileName);
        final FileValidator fileValidator = new FileValidatorImpl();
        Assert.assertTrue(fileValidator.validateFileFormat(inputFile));
    }

    /**
     * Helpers method
     */

    /**
     * Create a File object from a given file name
     *
     * @param fileName the given filename to be converted
     * @return the File obejct
     * @throws IOException
     */
    @Nonnull
    private File getFile(@Nonnull final String fileName) throws IOException {
        checkNull(fileName, "fileName");

        //Get file from resources folder
        final ClassLoader classLoader = getClass().getClassLoader();
        File file = null;
        if (classLoader.getResource(fileName) != null)
            file = new File(classLoader.getResource(fileName).getFile());
        else
            throw new FileNotFoundException(fileName + " file not found.");

        return file;
    }
}
