package honours.ing.banq.model;

import org.junit.Before;
import org.junit.Test;

import java.util.Currency;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Jeffrey Bakker
 */
public class MoneyTest {

    private static final Currency DEFAULT_CURRENCY = Currency.getInstance(Locale.FRANCE);
    private static final Currency FOREIGN_CURRENCY = Currency.getInstance(Locale.US);

    private Money a;
    private Money b;
    private Money c;

    @Before
    public void setup() {
        a = new Money(DEFAULT_CURRENCY);
        b = new Money(DEFAULT_CURRENCY, 100, (short) 10);
        c = new Money(FOREIGN_CURRENCY);
    }

    @Test
    public void testSetup() {
        // TEST A
        assertEquals(DEFAULT_CURRENCY, a.getCurrency());
        assertEquals(0, a.getMajor());
        assertEquals(0, a.getMinor());

        // TEST B
        assertEquals(DEFAULT_CURRENCY, b.getCurrency());
        assertEquals(100, b.getMajor());
        assertEquals(10, b.getMinor());

        // TEST C
        assertEquals(FOREIGN_CURRENCY, c.getCurrency());
        assertEquals(0, c.getMajor());
        assertEquals(0, c.getMinor());
    }

    @Test
    public void testDeposit() {
        Money amount = new Money(DEFAULT_CURRENCY, 80, (short) 12);

        // TEST A
        a.deposit(amount);
        assertEquals(amount.getMajor(), a.getMajor());
        assertEquals(amount.getMinor(), a.getMinor());

        // TEST B
        b.deposit(amount);
        assertEquals(amount.getMajor() + 100, b.getMajor());
        assertEquals(amount.getMinor() + 10, b.getMinor());
    }

    @Test
    public void testDepositFromNegative() {
        long major = b.getMajor();
        short minor = b.getMinor();

        // First create the amount to use for deposit and make the amount in b negative
        Money amount = new Money(DEFAULT_CURRENCY, 2 * major, (short) (2 * minor));
        b.setAmount(-major, (short) -minor);

        // Then do the deposit
        b.deposit(amount);

        // Then check
        assertEquals(major, b.getMajor());
        assertEquals(minor, b.getMinor());
    }

    @Test
    public void testNegativeDeposit() {
        Money amount = new Money(DEFAULT_CURRENCY, -100, (short) -10);

        try {
            a.deposit(amount);

            // a.deposit(amount) should have thrown an IllegalArgumentException, if that did not happen run the
            // following statement:
            assertTrue(false);
        } catch (IllegalArgumentException e) {
            // a.deposit(amount) has thrown an IllegalArgumentException. That is exactly what we wanted to happen, so
            // run the following statement:
            assertTrue(true);
        }

        // Ensure the amount stayed the same
        assertEquals(0, a.getMajor());
        assertEquals(0, a.getMinor());
    }

    @Test
    public void testWithdrawal() {
        Money amount = new Money(DEFAULT_CURRENCY, 80, (short) 12);

        // TEST A
        a.withdraw(amount);
        assertEquals(-amount.getMajor(), a.getMajor());
        assertEquals(-amount.getMinor(), a.getMinor());

        // TEST B
        b.withdraw(amount);
        assertEquals(100 - amount.getMajor(), b.getMajor());
        assertEquals(10 - amount.getMinor(), b.getMinor());
    }

    @Test
    public void testNegativeWithdrawal() {
        Money amount = new Money(DEFAULT_CURRENCY, -100, (short) -10);

        try {
            a.withdraw(amount);

            // a.deposit(amount) should have thrown an IllegalArgumentException, if that did not happen run the
            // following statement:
            assertTrue(false);
        } catch (IllegalArgumentException e) {
            // a.deposit(amount) has thrown an IllegalArgumentException. That is exactly what we wanted to happen, so
            // run the following statement:
            assertTrue(true);
        }

        // Ensure the amount stayed the same
        assertEquals(0, a.getMajor());
        assertEquals(0, a.getMinor());
    }

}
