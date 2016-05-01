package errors;

import org.apache.log4j.Logger;

import javax.annotation.Nonnull;

import static util.Validator.checkNull;

/**
 * Created by Sougata on 5/1/2016.
 */
public class ExceptionUtil {

    public static void logAndThrow(@Nonnull final Exception exception, @Nonnull final Logger logger) {
        checkNull(exception, "exception");
        checkNull(logger, "logger");

        logger.error(exception);
        try {
            throw exception;
        } catch (Exception e1) {
            logger.error(e1.getMessage());
        }
    }
}
