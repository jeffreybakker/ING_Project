package honours.ing.banq.event;

import honours.ing.banq.BoilerplateTest;
import honours.ing.banq.time.TimeServiceImpl;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.*;

/**
 * @author jeffrey
 * @since 12-8-17
 */
public class SavingsInterestEventTest extends BoilerplateTest {

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
        accountService.openSavingsAccount(account1.token, account1.iBan);
        transactionService.depositIntoAccount(account1.iBan + "S", account1.cardNumber, account1.pin, 1000.0);

        testSavingsBalance(account1, 1000.0, 0.01);

        timeService.simulateTime(365);

        testSavingsBalance(account1, 1001.50, 0.01);
    }

}