package honours.ing.banq;

import honours.ing.banq.account.BankAccountService;
import honours.ing.banq.account.bean.NewAccountBean;
import honours.ing.banq.auth.AuthService;
import honours.ing.banq.config.TestConfiguration;
import honours.ing.banq.info.InfoService;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.transaction.Transactional;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

/**
 * @author Kevin Witlox
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@Import(TestConfiguration.class)
@Transactional
@ActiveProfiles("test")
public class BoilerplateTest {

    // Services
    @Autowired
    protected BankAccountService bankAccountService;

    @Autowired
    protected AuthService authService;

    @Autowired
    protected InfoService infoService;

    // Fields
    protected NewAccountBean account1, account2;
    protected String tokenAccount1, tokenAccount2;

    @Before
    public void setUp() throws Exception {
        account1 = bankAccountService.openAccount("Jan", "Jansen", "J.", "1996-1-1",
                "1234567890", "Klaverstraat 1", "0612345678", "janjansen@gmail.com", "jantje96",
                "1234");
        account2 = bankAccountService.openAccount("Piet", "Pietersen", "p.p", "1998-8-8",
                "012345789", "Huisstraat 1", "0607080910", "piet@gmail.com", "piet1", "1234");

        tokenAccount1 = authService.getAuthToken("jantje96", "1234").getAuthToken();
        tokenAccount2 = authService.getAuthToken("piet1", "1234").getAuthToken();

        assertThat(infoService.getBalance(tokenAccount1, account1.getiBAN()).getBalance(), equalTo
                (0d));
        assertThat(infoService.getBalance(tokenAccount2, account2.getiBAN()).getBalance(), equalTo
                (0d));
    }

    @After
    public void tearDown() throws Exception {
        bankAccountService.closeAccount(tokenAccount1, account1.getiBAN());
        bankAccountService.closeAccount(tokenAccount2, account2.getiBAN());
    }

}
