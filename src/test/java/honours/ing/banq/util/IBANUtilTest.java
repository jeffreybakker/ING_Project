package honours.ing.banq.util;

import org.junit.Ignore;
import org.junit.Test;

import static honours.ing.banq.util.IBANUtil.*;
import static org.junit.Assert.*;

/**
 * Created by jeffrey on 31-5-17.
 */
public class IBANUtilTest {

    private static final int MAX_ACCOUNT_NUMBER_LENGTH = 10;

    @Test
    public void testGenerateIBAN() {
        for (int i = 0; i <= MAX_ACCOUNT_NUMBER_LENGTH; i++) {
            long accountNumber = (long) (Math.pow(10, i) * 0.9);
            String iban = generateIBAN(accountNumber);

            assertTrue(isValidIBAN(iban));
            assertTrue(isValidIBAN(iban + "S"));
            assertEquals(accountNumber, getAccountNumber(iban));
            assertEquals(accountNumber, getAccountNumber(iban + "S"));
        }
    }

    @Ignore
    @Test
    public void testValid() {
        // Test iBAN of University of Twente
        assertTrue(isValidIBAN("NL33ABNA0405323972"));
        // Should be invalid with different check numbers
        assertFalse(isValidIBAN("NL00ABNA0405323972"));

        // Test iBAN of DUO
        assertTrue(isValidIBAN("NL45INGB0705001903"));
        // Should be invalid with different check numbers
        assertFalse(isValidIBAN("NL00INGB0705001903"));

        // Test some random strings
        assertFalse(isValidIBAN(""));
        assertFalse(isValidIBAN("abcdefg"));
        assertFalse(isValidIBAN("123456798"));
        assertFalse(isValidIBAN("dslk;jfidsfjq8ej fi jfakld;jfkadjfalkdsjfasd fa"));
        assertFalse(isValidIBAN("NL01RABO23456789"));
    }

}
