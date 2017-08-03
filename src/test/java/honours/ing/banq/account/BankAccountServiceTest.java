package honours.ing.banq.account;

import honours.ing.banq.InvalidParamValueError;
import honours.ing.banq.account.bean.NewAccountBean;
import honours.ing.banq.auth.AuthService;
import honours.ing.banq.bean.AccountInfo;
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

import java.util.Calendar;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * @author jeffrey
 * @since 9-6-17
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@Import(TestConfiguration.class)
@ActiveProfiles("test")
public class BankAccountServiceTest {

    // Services
    @Autowired
    private BankAccountService service;

    @Autowired
    private AuthService authService;

    // Variables
    private String username;
    private String password;
    private String token;
    private String ssn;
    private String email;

    private Queue<String> accounts;

    @Before
    public void setUp() throws Exception {
        username = StringUtil.generate(10);
        password = StringUtil.generate(10);
        ssn = StringUtil.generate(10);
        email = StringUtil.generate(10);

        accounts = new ArrayBlockingQueue<>(10, true);

        // First account
        NewAccountBean account = service.openAccount(
                "University", "of Twente", "UT",
                "1996-1-1", ssn,
                "Universiteitsstraat 1, Enschede", "06-12345678",
                StringUtil.generate(10), username, password);
        accounts.add(account.getiBAN());

        // Get the auth token
        token = authService.getAuthToken(username, password).getAuthToken();

        // And a few more
        for (int i = 0; i < 4; i++) {
            account = service.openAdditionalAccount(token);
            accounts.add(account.getiBAN());
        }
    }

    @After
    public void tearDown() throws Exception {
        while (!accounts.isEmpty()) {
            service.closeAccount(token, accounts.poll());
        }
    }

    @Test
    public void openAccount() throws Exception {
        tearDown();

        username = StringUtil.generate(10);
        password = StringUtil.generate(10);
        ssn = StringUtil.generate(10);
        email = StringUtil.generate(10);

        NewAccountBean account = service.openAccount(
                "University", "of Twente", "UT",
                "1996-1-1", StringUtil.generate(10),
                "Universiteitsstraat 1, Enschede", "06-12345678",
                email, username, password);
        accounts.add(account.getiBAN());

        // Get the auth token
        token = authService.getAuthToken(username, password).getAuthToken();
    }

    @Test(expected = InvalidParamValueError.class)
    public void openAccountNonUniqueSSN() throws Exception {
        service.openAccount(
                "University", "of Twente", "UT",
                "1996-1-1", ssn,
                "Universiteitsstraat 1, Enschede", "06-12345678",
                StringUtil.generate(10), StringUtil.generate(10), StringUtil.generate(10));
    }

    @Test
    public void openAccountNonUniqueEmail() throws Exception {
        service.openAccount(
                "University", "of Twente", "UT",
                "1996-1-1", StringUtil.generate(10),
                "Universiteitsstraat 1, Enschede", "06-12345678",
                email, StringUtil.generate(10), password);
    }

    @Test
    public void openAccountNonUniqueUsername() throws Exception {
        service.openAccount(
                "University", "of Twente", "UT",
                "1996-1-1", StringUtil.generate(10),
                "Universiteitsstraat 1, Enschede", "06-12345678",
                StringUtil.generate(10), StringUtil.generate(10), StringUtil.generate(10));
    }

    @Test
    public void openAdditionalAccount() throws Exception {
        NewAccountBean account = service.openAdditionalAccount(token);
        accounts.add(account.getiBAN());
    }

    @Test
    public void closeAccount() throws Exception {
        if (!accounts.isEmpty()) {
            service.closeAccount(token, accounts.poll());
        }
    }

    @Test(expected = InvalidParamValueError.class)
    public void closeNonExistingAccount() throws Exception {
        service.closeAccount(token, IBANUtil.generateIBAN(123456789));
    }

}