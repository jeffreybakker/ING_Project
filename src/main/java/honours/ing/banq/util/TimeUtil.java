package honours.ing.banq.util;

import java.util.Date;

/**
 * A utility class for the time simulation.
 * @author Jeffrey Bakker
 * @since 4-8-17
 */
public class TimeUtil {

    /**
     * Returns the Date object representing the current time in the system (may be in the future).
     * @return a Date object
     */
    public static Date getDate() {
        return new Date();
    }

    /**
     * Returns the system's current time in milliseconds (may be in the future).
     * @return the time in milliseconds since January 1, 1970
     */
    public static long currentTimeMillis() {
        return System.currentTimeMillis();
    }

}
