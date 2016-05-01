package errors;

import org.apache.log4j.Logger;

import javax.annotation.Nonnull;

import static util.Validator.checkNull;

/**
 * Created by Sougata on 5/1/2016.
 */
public class ExceptionUtil {

    /**
     * The method is responsible for log and throw a given exception.
     *
     * @param exception the given exception to be thrown and logged
     * @param logger    logger of the class
     */
    public static void logAndThrow(@Nonnull final Exception exception, @Nonnull final Logger logger) {
        checkNull(exception, "exception");
        checkNull(logger, "logger");

        logger.error(exception);
        try {
            throw exception;
        } catch (final Exception ex) {
            logger.error(ex.getMessage());
        }
    }
}
