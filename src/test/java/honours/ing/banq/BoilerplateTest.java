package honours.ing.banq;

import honours.ing.banq.account.BankAccountService;
import honours.ing.banq.auth.AuthService;
import honours.ing.banq.bean.AccountInfo;
import honours.ing.banq.config.TestConfiguration;
import honours.ing.banq.info.InfoService;
import honours.ing.banq.time.TimeService;
import honours.ing.banq.transaction.TransactionService;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.transaction.Transactional;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * @author Kevin Witlox
 */
@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@Import(TestConfiguration.class)
@Transactional
@ActiveProfiles("test")
public class BoilerplateTest {

    protected final long MAX_TIME_DIFF = 1000;

    // Services
    @Autowired
    protected BankAccountService accountService;

    @Autowired
    protected AuthService authService;

    @Autowired
    protected InfoService infoService;

    @Autowired
    protected TimeService timeService;

    @Autowired
    protected TransactionService transactionService;

    // Fields
    protected AccountInfo account1, account2;

    @Before
    public void setUp() throws Exception {
        timeService.reset();
        assertTrue((System.currentTimeMillis() - MAX_TIME_DIFF) <= timeService.getDate().getDate().getTime());
        assertTrue((System.currentTimeMillis() + MAX_TIME_DIFF) >= timeService.getDate().getDate().getTime());

        account1 = new AccountInfo(
                accountService.openAccount(
                        "Jan", "Jansen", "J.", "1996-1-1",
                        "1234567890", "Klaverstraat 1", "0612345678",
                        "janjansen@gmail.com", "jantje96", "1234"),
                "jantje96", "1234");
        account2 = new AccountInfo(
                accountService.openAccount("Piet", "Pietersen", "p.p", "1998-8-8",
                        "012345789", "Huisstraat 1", "0607080910",
                        "piet@gmail.com", "piet1", "1234"),
                "piet1", "1234");

        account1.token = authService.getAuthToken("jantje96", "1234").getAuthToken();
        account2.token = authService.getAuthToken("piet1", "1234").getAuthToken();

        assertThat(infoService.getBalance(account1.token, account1.iBan).getBalance(), equalTo(0d));
        assertThat(infoService.getBalance(account2.token, account2.iBan).getBalance(), equalTo(0d));
    }

    @After
    public void tearDown() throws Exception {
        account1.token = authService.getAuthToken("jantje96", "1234").getAuthToken();
        account2.token = authService.getAuthToken("piet1", "1234").getAuthToken();

        accountService.closeAccount(account1.token, account1.iBan);
        accountService.closeAccount(account2.token, account2.iBan);

        timeService.reset();
    }

    protected void testBalance(AccountInfo account, double expected, double accuracy) throws Exception {
        account.token = authService.getAuthToken(account.username, account.password).getAuthToken();
        assertEquals(expected, infoService.getBalance(account.token, account.iBan).getBalance(), accuracy);
    }

}
