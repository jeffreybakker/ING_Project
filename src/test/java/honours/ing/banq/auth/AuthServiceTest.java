package honours.ing.banq.auth;

import honours.ing.banq.InvalidParamValueError;
import honours.ing.banq.account.BankAccount;
import honours.ing.banq.account.BankAccountService;
import honours.ing.banq.account.bean.NewAccountBean;
import honours.ing.banq.bean.AccountInfo;
import honours.ing.banq.customer.Customer;
import honours.ing.banq.time.TimeService;
import honours.ing.banq.util.IBANUtil;
import honours.ing.banq.util.StringUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

/**
 * @author jeffrey
 * @since 9-6-17
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@Import(TestConfiguration.class)
@ActiveProfiles("test")
public class AuthServiceTest {

    // Services
    @Autowired
    private AuthService service;

    @Autowired
    private BankAccountService accountService;

    @Autowired
    private TimeService timeService;

    // Variables
    private AccountInfo accountInfo;

    @Before
    public void setUp() throws Exception {
        String username = StringUtil.generate(10);
        String password = StringUtil.generate(10);

        NewAccountBean account = accountService.openAccount(
                "University", "of Twente", "UT",
                "1996-1-1", StringUtil.generate(10),
                "Universiteitsstraat 1, Enschede", "06-12345678",
                StringUtil.generate(10), username, password);

        accountInfo = new AccountInfo(account, username, password);
        accountInfo.token = service.getAuthToken(username, password).getAuthToken();
    }

    @After
    public void tearDown() throws Exception {
        accountInfo.token = service.getAuthToken(accountInfo.username, accountInfo.password).getAuthToken();
        accountService.closeAccount(accountInfo.token, accountInfo.iBan);
    }

    @Test
    public void getAuthToken() throws Exception {
        String token = service.getAuthToken(accountInfo.username, accountInfo.password).getAuthToken();
        accountInfo.token = token;

        assertNotNull(token);
        assertTrue(token.length() > 0);
    }

    @Test
    public void getAuthTokenInvalidUsername() throws Exception {
        try {
            service.getAuthToken("", accountInfo.password);
            fail();
        } catch (AuthenticationError ignored) {
        }
    }

    @Test
    public void getAuthTokenInvalidPassword() throws Exception {
        try {
            service.getAuthToken(accountInfo.username, "");
            fail();
        } catch (AuthenticationError ignored) {
        }
    }

    @Test
    public void getAuthTokenInvalidUsernameAndPassword() throws Exception {
        try {
            service.getAuthToken("", "");
            fail();
        } catch (AuthenticationError ignored) {
        }
    }

    @Test
    public void getAuthorizedCustomer() throws Exception {
        String token = service.getAuthToken(accountInfo.username, accountInfo.password).getAuthToken();

        assertNotNull(token);
        assertTrue(token.length() > 0);

        Customer customer = service.getAuthorizedCustomer(token);
        assertEquals(accountInfo.username, customer.getUsername());
        assertEquals(accountInfo.password, customer.getPassword());
    }

    @Test(expected = NotAuthorizedError.class)
    public void getAuthorizedCustomerExpiredToken() throws Exception {
        String token = service.getAuthToken(accountInfo.username, accountInfo.password).getAuthToken();

        assertNotNull(token);
        assertTrue(token.length() > 0);

        timeService.simulateTime(50);

        service.getAuthorizedCustomer(token);
    }

    @Test
    public void getAuthorizedCustomerNullToken() throws Exception {
        try {
            service.getAuthorizedCustomer(null);
            fail();
        } catch (InvalidParamValueError ignored) {
        }
    }

    @Test
    public void getAuthorizedCustomerEmptyToken() throws Exception {
        try {
            service.getAuthorizedCustomer("");
            fail();
        } catch (InvalidParamValueError ignored) {
        }
    }

    @Test
    public void getAuthorizedCustomerInvalidToken() throws Exception {
        try {
            service.getAuthorizedCustomer("invalidToken");
            fail();
        } catch (NotAuthorizedError ignored) {
        }
    }

    @Test
    public void getAuthorizedAccount() throws Exception {
        BankAccount account = service.getAuthorizedBankAccount(accountInfo.iBan, accountInfo.cardNumber, accountInfo.pin);

        assertNotNull(account);
        assertEquals(accountInfo.iBan, IBANUtil.generateIBAN(account));
    }

    @Test
    public void getAuthorizedAccountInvalidIBan() throws Exception {
        try {
            service.getAuthorizedBankAccount(null, accountInfo.cardNumber, accountInfo.pin);
            fail();
        } catch (InvalidParamValueError ignored) {
        }

        try {
            service.getAuthorizedBankAccount("", accountInfo.cardNumber, accountInfo.pin);
            fail();
        } catch (InvalidParamValueError ignored) {
        }

        try {
            service.getAuthorizedBankAccount("NL45INGB0705001903", accountInfo.cardNumber, accountInfo.pin);
            fail();
        } catch (InvalidParamValueError ignored) {
        }
    }

    @Test
    public void getAuthorizedAccountInvalidCardNumber() throws Exception {
        try {
            service.getAuthorizedBankAccount(accountInfo.iBan, null, accountInfo.pin);
            fail();
        } catch (InvalidParamValueError ignored) {
        }

        try {
            service.getAuthorizedBankAccount(accountInfo.iBan, "", accountInfo.pin);
            fail();
        } catch (InvalidParamValueError ignored) {
        }

        try {
            service.getAuthorizedBankAccount(accountInfo.iBan, "-1", accountInfo.pin);
            fail();
        } catch (InvalidParamValueError ignored) {
        }
    }

    @Test
    public void getAuthorizedAccountInvalidPin() throws Exception {
        try {
            service.getAuthorizedBankAccount(accountInfo.iBan, accountInfo.cardNumber, null);
            fail();
        } catch (InvalidParamValueError ignored) {
        }

        try {
            service.getAuthorizedBankAccount(accountInfo.iBan, accountInfo.cardNumber, "");
            fail();
        } catch (InvalidPINError ignored) {
        }
    }

}