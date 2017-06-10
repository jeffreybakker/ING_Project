package honours.ing.banq.transaction;

import honours.ing.banq.InvalidParamValueError;
import honours.ing.banq.account.BankAccountService;
import honours.ing.banq.account.bean.NewAccountBean;
import honours.ing.banq.auth.AuthService;
import honours.ing.banq.auth.InvalidPINError;
import honours.ing.banq.auth.NotAuthorizedError;
import honours.ing.banq.config.TestConfiguration;
import honours.ing.banq.info.InfoService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.transaction.Transactional;
import java.util.Date;

import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * @author Kevin Witlox
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@Import(TestConfiguration.class)
@Transactional
@ActiveProfiles("test")
public class TransactionServiceTest {

    // Services
    @Autowired
    private TransactionService transactionService;

    @Autowired
    private BankAccountService bankAccountService;

    @Autowired
    private InfoService infoService;

    @Autowired
    private AuthService authService;

    // Fields
    private NewAccountBean account1, account2;
    private String tokenAccount1, tokenAccount2;

    @Before
    public void setUp() throws Exception {
        account1 = bankAccountService.openAccount("Jan", "Jansen", "J.", new Date(1996, 1, 1),
                "1234567890", "Klaverstraat 1", "0612345678", "janjansen@gmail.com", "jantje96",
                "1234");
        account2 = bankAccountService.openAccount("Piet", "Pietersen", "p.p", new Date(1998, 8, 8),
                "012345789", "Huisstraat 1", "0607080910", "piet@gmail.com", "piet1", "1234");

        tokenAccount1 = authService.getAuthToken("jantje96", "1234");
        tokenAccount2 = authService.getAuthToken("piet1", "1234");

        assertThat(infoService.getBalance(tokenAccount1, account1.getiBAN()).getBalance(), equalTo
                (0d));
        assertThat(infoService.getBalance(tokenAccount2, account2.getiBAN()).getBalance(), equalTo
                (0d));
    }

    @Test
    public void depositIntoAccount() throws Exception {
        transactionService.depositIntoAccount(account1.getiBAN(), account1.getPinCard
                (), account1.getPinCode(), 200.0d);
        assertThat(infoService.getBalance(tokenAccount1, account1.getiBAN()).getBalance(), equalTo
                (200d));
    }

    @Test(expected = InvalidPINError.class)
    public void depositIntoAccountWrongPinCode() throws Exception {
        transactionService.depositIntoAccount(account1.getiBAN(), account1.getPinCard
                (), 9999, 200.0d);
        assertThat(infoService.getBalance(tokenAccount1, account1.getiBAN()).getBalance(), equalTo
                (0d));
    }

    @Test(expected = InvalidParamValueError.class)
    public void depositIntoAccountNegativeAmount() throws Exception {
        transactionService.depositIntoAccount(account1.getiBAN(), account1.getPinCard
                (), account1.getPinCode(), -200d);
        assertThat(infoService.getBalance(tokenAccount1, account1.getiBAN()).getBalance(), equalTo
                (0d));
    }

    @Test(expected = InvalidParamValueError.class)
    public void depositIntoAccountWrongIBAN() throws Exception {
        transactionService.depositIntoAccount(account2.getiBAN(), account1.getPinCard
                (), account1.getPinCode(), 200d);
        assertThat(infoService.getBalance(tokenAccount1, account1.getiBAN()).getBalance(), equalTo
                (0d));
    }

    @Test(expected = InvalidParamValueError.class)
    public void depositIntoAccountWrongIBANType() throws Exception {
        transactionService.depositIntoAccount("null", account1.getPinCard
                (), account1.getPinCode(), 200d);
        assertThat(infoService.getBalance(tokenAccount1, account1.getiBAN()).getBalance(), equalTo
                (0d));
    }

    @Test(expected = InvalidParamValueError.class)
    public void depositIntoAccountWrongPinCard() throws Exception {
        transactionService.depositIntoAccount(account1.getiBAN(), 0, account1.getPinCode(), 200d);
        assertThat(infoService.getBalance(tokenAccount1, account1.getiBAN()).getBalance(), equalTo
                (0d));
    }


    @Test
    public void payFromAccount() throws Exception {
        // Make sure source iban has balance
        transactionService.depositIntoAccount(account1.getiBAN(), account1.getPinCard(),
                account1.getPinCode(), 200d);

        // Transaction
        transactionService.payFromAccount(account1.getiBAN(), account2.getiBAN(),
                account1.getPinCard(), account1.getPinCode(), 200d);
        assertThat(infoService.getBalance(tokenAccount1, account1.getiBAN()).getBalance(), equalTo
                (0d));
        assertThat(infoService.getBalance(tokenAccount2, account2.getiBAN()).getBalance(), equalTo
                (200d));
    }

    @Test(expected = InvalidPINError.class)
    public void payFromAccountWrongPinCode() throws Exception {
        // Make sure source iban has balance
        transactionService.depositIntoAccount(account1.getiBAN(), account1.getPinCard(),
                account1.getPinCode(), 200d);

        // Transaction
        transactionService.payFromAccount(account1.getiBAN(), account2.getiBAN(),
                account1.getPinCard(), -1, 200d);
        assertThat(infoService.getBalance(tokenAccount1, account1.getiBAN()).getBalance(), equalTo
                (200d));
        assertThat(infoService.getBalance(tokenAccount2, account2.getiBAN()).getBalance(), equalTo
                (0d));
    }

    @Test(expected = InvalidParamValueError.class)
    public void payFromAccountWrongPinCard() throws Exception {
        // Make sure source iban has balance
        transactionService.depositIntoAccount(account1.getiBAN(), account1.getPinCard(),
                account1.getPinCode(), 200d);

        // Transaction
        transactionService.payFromAccount(account1.getiBAN(), account2.getiBAN(),
                -1, account1.getPinCode(), 200d);
        assertThat(infoService.getBalance(tokenAccount1, account1.getiBAN()).getBalance(), equalTo
                (200d));
        assertThat(infoService.getBalance(tokenAccount2, account2.getiBAN()).getBalance(), equalTo
                (0d));
    }

    @Test(expected = InvalidParamValueError.class)
    public void payFromAccountWrongSourceIBAN() throws Exception {
        // Make sure source iban has balance
        transactionService.depositIntoAccount(account1.getiBAN(), account1.getPinCard(),
                account1.getPinCode(), 200d);

        // Transaction
        transactionService.payFromAccount("null", account2.getiBAN(),
                account1.getPinCard(), account1.getPinCode(), 200d);
        assertThat(infoService.getBalance(tokenAccount1, account1.getiBAN()).getBalance(), equalTo
                (200d));
    }

    @Test(expected = InvalidParamValueError.class)
    public void payFromAccountWrongTargetIBAN() throws Exception {
        // Make sure source iban has balance
        transactionService.depositIntoAccount(account1.getiBAN(), account1.getPinCard(),
                account1.getPinCode(), 200d);

        // Transaction
        transactionService.payFromAccount(account1.getiBAN(), "null",
                account1.getPinCard(), account1.getPinCode(), 200d);
        assertThat(infoService.getBalance(tokenAccount1, account1.getiBAN()).getBalance(), equalTo
                (200d));
        assertThat(infoService.getBalance(tokenAccount2, account2.getiBAN()).getBalance(), equalTo
                (0d));
    }

    @Test(expected = InvalidParamValueError.class)
    public void payFromAccountNotEnoughBalance() throws Exception {
        // Make sure source iban has balance
        transactionService.depositIntoAccount(account1.getiBAN(), account1.getPinCard(),
                account1.getPinCode(), 200d);
        assertThat(infoService.getBalance(tokenAccount1, account1.getiBAN()).getBalance(), equalTo
                (200d));

        // Transaction
        transactionService.payFromAccount(account1.getiBAN(), account2.getiBAN(),
                account1.getPinCard(), account1.getPinCode(), 201d);
        assertThat(infoService.getBalance(tokenAccount1, account1.getiBAN()).getBalance(), equalTo
                (200d));
        assertThat(infoService.getBalance(tokenAccount2, account2.getiBAN()).getBalance(), equalTo
                (0d));
    }

    @Test(expected = InvalidParamValueError.class)
    public void payFromAccountNegativeAmount() throws Exception {
        // Make sure source iban has balance
        transactionService.depositIntoAccount(account1.getiBAN(), account1.getPinCard(),
                account1.getPinCode(), 200d);
        assertThat(infoService.getBalance(tokenAccount1, account1.getiBAN()).getBalance(), equalTo
                (200d));

        // Transaction
        transactionService.payFromAccount(account1.getiBAN(), account2.getiBAN(),
                account1.getPinCard(), account1.getPinCode(), -200d);
        assertThat(infoService.getBalance(tokenAccount1, account1.getiBAN()).getBalance(), equalTo
                (200d));
        assertThat(infoService.getBalance(tokenAccount2, account2.getiBAN()).getBalance(), equalTo
                (0d));
    }

    @Test
    public void transferMoney() throws Exception {
        transactionService.depositIntoAccount(account1.getiBAN(), account1.getPinCard(), account1.getPinCode(), 200d);

        transactionService.transferMoney(tokenAccount1, account1.getiBAN(), account2.getiBAN(), "Piet Pietersen", 200d, "Geld");
        assertThat(infoService.getBalance(tokenAccount1, account1.getiBAN()).getBalance(), equalTo
                (0d));
        assertThat(infoService.getBalance(tokenAccount2, account2.getiBAN()).getBalance(), equalTo
                (200d));
    }

    @Test(expected = NotAuthorizedError.class)
    public void transferMoneyNotAuthorized() throws Exception {
        transactionService.depositIntoAccount(account1.getiBAN(), account1.getPinCard(), account1.getPinCode(), 200d);

        transactionService.transferMoney(tokenAccount2, account1.getiBAN(), account2.getiBAN(), "Piet Pietersen", 200d, "Geld");
        assertThat(infoService.getBalance(tokenAccount1, account1.getiBAN()).getBalance(), equalTo
                (200d));
        assertThat(infoService.getBalance(tokenAccount2, account2.getiBAN()).getBalance(), equalTo
                (0d));
    }

    @Test(expected = NotAuthorizedError.class)
    public void transferMoneyUnauthorizedSourceIBAN() throws Exception {
        transactionService.depositIntoAccount(account1.getiBAN(), account1.getPinCard(), account1.getPinCode(), 200d);

        transactionService.transferMoney(tokenAccount1, account2.getiBAN(), account2.getiBAN(), "Piet Pietersen", 200d, "Geld");
        assertThat(infoService.getBalance(tokenAccount1, account1.getiBAN()).getBalance(), equalTo
                (200d));
        assertThat(infoService.getBalance(tokenAccount2, account2.getiBAN()).getBalance(), equalTo
                (0d));
    }

    @Test(expected = InvalidParamValueError.class)
    public void transferMoneyNonExistantTargetIBAN() throws Exception {
        transactionService.depositIntoAccount(account1.getiBAN(), account1.getPinCard(), account1.getPinCode(), 200d);

        transactionService.transferMoney(tokenAccount1, account1.getiBAN(), "null", "Piet Pietersen", 200d, "Geld");
        assertThat(infoService.getBalance(tokenAccount1, account1.getiBAN()).getBalance(), equalTo
                (200d));
        assertThat(infoService.getBalance(tokenAccount2, account2.getiBAN()).getBalance(), equalTo
                (0d));
    }

    @Test(expected = InvalidParamValueError.class)
    public void transferMoneyNegativeBalance() throws Exception {
        transactionService.transferMoney(tokenAccount1, account1.getiBAN(), account2.getiBAN(), "Piet Pietersen", -200d, "Geld");
        assertThat(infoService.getBalance(tokenAccount1, account1.getiBAN()).getBalance(), equalTo
                (200d));
        assertThat(infoService.getBalance(tokenAccount2, account2.getiBAN()).getBalance(), equalTo
                (0d));
    }

    @Test(expected = InvalidParamValueError.class)
    public void transferMoneyNotEnoughBalance() throws Exception {
        transactionService.depositIntoAccount(account1.getiBAN(), account1.getPinCard(), account1.getPinCode(), 200d);

        transactionService.transferMoney(tokenAccount1, account1.getiBAN(), account2.getiBAN(), "Piet Pietersen", 201d, "Geld");
        assertThat(infoService.getBalance(tokenAccount1, account1.getiBAN()).getBalance(), equalTo
                (200d));
        assertThat(infoService.getBalance(tokenAccount2, account2.getiBAN()).getBalance(), equalTo
                (0d));
    }

}