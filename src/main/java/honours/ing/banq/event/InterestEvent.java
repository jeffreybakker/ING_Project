package honours.ing.banq.event;

import java.util.Calendar;
import java.util.Date;

import static java.util.Calendar.*;
import static java.util.Calendar.DAY_OF_MONTH;

/**
 * @author jeffrey
 * @since 12-8-17
 */
public abstract class InterestEvent implements Event {

    @Override
    public long nextIteration(long lastIteration) {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date(lastIteration));

        c.set(HOUR_OF_DAY, 0);
        c.set(MINUTE, 0);
        c.set(SECOND, 0);
        c.set(MILLISECOND, 0);

        c.add(DAY_OF_MONTH, 1);
        return c.getTimeInMillis();
    }

}
