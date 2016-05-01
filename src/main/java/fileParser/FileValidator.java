package fileParser;

import errors.InvalidFormatException;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;

/**
 * Created by Sougata on 4/30/2016.
 */
public interface FileValidator {

    /**
     * Validate the input file with the defined rules. If validates then return true otherwise false.
     *
     * @param file input file to be validated
     * @return true | false
     * @throws IOException
     * @throws InvalidFormatException
     */
    @Nonnull
    boolean validateFileFormat(@Nonnull File file) throws IOException, InvalidFormatException;
}
