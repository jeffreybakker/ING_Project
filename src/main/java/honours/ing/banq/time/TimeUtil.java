package honours.ing.banq.time;

import java.util.Date;

/**
 * A utility class for the time simulation.
 * @author Jeffrey Bakker
 * @since 4-8-17
 */
public class TimeUtil {

    private static final long DAY = 1000 * 60 * 60 * 24; // second * minute * hour * day

    private static long addedMillis = 0;

    /**
     * Simulates the passing of x days.
     * @param days the amount of days
     */
    public static void addDays(int days) {
        if (days < 0) {
            throw new IllegalArgumentException("The amount of days may not be less than 0");
        }

        addedMillis += days * DAY;
    }

    /**
     * Resets the simulation of time.
     */
    public static void resetTime() {
        addedMillis = 0;
    }

    /**
     * Returns the Date object representing the current time in the system (may be in the future).
     * @return a Date object
     */
    public static Date getDate() {
        return new Date(currentTimeMillis());
    }

    /**
     * Returns the system's current time in milliseconds (may be in the future).
     * @return the time in milliseconds since January 1, 1970
     */
    public static long currentTimeMillis() {
        return System.currentTimeMillis() + addedMillis;
    }

}
