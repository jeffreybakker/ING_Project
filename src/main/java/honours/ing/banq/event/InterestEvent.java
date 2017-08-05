package honours.ing.banq.event;

import honours.ing.banq.time.TimeServiceImpl;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;

import static java.util.Calendar.*;

/**
 * @author jeffrey
 * @since 5-8-17
 */
@Component
public class InterestEvent implements Event {



    @Override
    public void execute(long time) {

    }

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
