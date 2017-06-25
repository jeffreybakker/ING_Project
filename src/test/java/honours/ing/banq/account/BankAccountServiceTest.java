package honours.ing.banq.account;

import honours.ing.banq.account.bean.NewAccountBean;
import honours.ing.banq.auth.AuthService;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;

import static org.junit.Assert.*;

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

    private Queue<String> accounts;

    @Before
    public void setUp() throws Exception {
        username = StringUtil.generate(10);
        password = StringUtil.generate(10);

        accounts = new ArrayBlockingQueue<>(10, true);

        // First account
        NewAccountBean account = service.openAccount(
                "University", "of Twente", "UT",
                Calendar.getInstance().getTime(), StringUtil.generate(10),
                "Universiteitsstraat 1, Enschede", "06-12345678",
                StringUtil.generate(10), username, password);
        accounts.add(account.getiBAN());

        // Get the auth token
        authService.getAuthToken(username, password);

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

        NewAccountBean account = service.openAccount(
                "University", "of Twente", "UT",
                Calendar.getInstance().getTime(), StringUtil.generate(10),
                "Universiteitsstraat 1, Enschede", "06-12345678",
                StringUtil.generate(10), username, password);
    }

    @Test
    public void openAdditionalAccount() throws Exception {
    }

    @Test
    public void closeAccount() throws Exception {
    }

}