package honours.ing.banq.event;

import honours.ing.banq.BoilerplateTest;
import honours.ing.banq.time.TimeServiceImpl;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

/**
 * @author jeffrey
 * @since 5-8-17
 */
public class OverdraftInterestEventTest extends BoilerplateTest {

    @Test
    public void testInterest() throws Exception {
        long now = TimeServiceImpl.currentTimeMillis();

        Calendar c = Calendar.getInstance();
        c.setTime(new Date(now));
        c.add(Calendar.YEAR, 1);
        c.set(Calendar.MONTH, Calendar.JANUARY);
        c.set(Calendar.DAY_OF_MONTH, 1);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);

        long nextJan = c.getTimeInMillis();
        long diff = (nextJan - now) / (1000 * 60 * 60 * 24) + 1;

        // Shift the time to the first of january so that all the math is correct
        timeService.simulateTime((int) diff);

        account1.token = authService.getAuthToken(account1.username, account1.password).getAuthToken();
        accountService.setOverdraftLimit(account1.token, account1.iBan, 1000.0);
        transactionService.transferMoney(
                account1.token, account1.iBan,
                account2.iBan, "Piet",
                1000.0, "Ha maat");

        testBalance(account1, -1000.0, 0.01);
        testBalance(account2, 1000.0, 0.01);

        timeService.simulateTime(365);

        testBalance(account1, -1100.0, 0.01);
        testBalance(account2, 1000.0, 0.01);
    }

}