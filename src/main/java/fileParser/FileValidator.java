package fileParser;

import errors.InvalidFormatException;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;

/**
 * Created by Sougata on 4/30/2016.
 */
public interface FileValidator {

    @Nonnull
    boolean validateFileFormat(@Nonnull File file) throws IOException, InvalidFormatException;
}
