package honours.ing.banq.time;

import honours.ing.banq.BoilerplateTest;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Jeffrey Bakker
 * @since 4-8-17
 */
public class TimeServiceTest extends BoilerplateTest {

    private static final long DAY = 1000 * 60 * 60 * 24;

    @Test
    public void simulateTime() throws Exception {
        timeService.simulateTime(1);
        assertTrue((System.currentTimeMillis() + DAY - MAX_TIME_DIFF) <= timeService.getDate().getDate().getTime());
        assertTrue((System.currentTimeMillis() + DAY + MAX_TIME_DIFF) >= timeService.getDate().getDate().getTime());
    }

    @Test
    public void getDate() throws Exception {
        assertTrue((System.currentTimeMillis() - MAX_TIME_DIFF) <= timeService.getDate().getDate().getTime());
        assertTrue((System.currentTimeMillis() + MAX_TIME_DIFF) >= timeService.getDate().getDate().getTime());

        timeService.simulateTime(1);

        assertTrue((System.currentTimeMillis() + DAY - MAX_TIME_DIFF) <= timeService.getDate().getDate().getTime());
        assertTrue((System.currentTimeMillis() + DAY + MAX_TIME_DIFF) >= timeService.getDate().getDate().getTime());

    }

}