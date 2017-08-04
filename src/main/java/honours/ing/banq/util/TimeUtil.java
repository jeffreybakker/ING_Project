package honours.ing.banq.util;

import java.util.Date;

/**
 * A utility class for the time simulation.
 * @author Jeffrey Bakker
 * @since 4-8-17
 */
public class TimeUtil {

    public static Date getDate() {
        return new Date();
    }

    public static long currentTimeMillis() {
        return System.currentTimeMillis();
    }

}
