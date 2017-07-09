package honours.ing.banq.transaction;

import honours.ing.banq.BoilerplateTest;
import honours.ing.banq.InvalidParamValueError;
import honours.ing.banq.auth.InvalidPINError;
import honours.ing.banq.auth.NotAuthorizedError;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

/**
 * @author Kevin Witlox
 */
public class TransactionServiceTest extends BoilerplateTest {

    // Services
    @Autowired
    private TransactionService transactionService;

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
                (), "9999", 200.0d);
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
        transactionService.depositIntoAccount("null", account1.getPinCard(), account1.getPinCode(), 200d);
        assertThat(infoService.getBalance(tokenAccount1, account1.getiBAN()).getBalance(), equalTo
                (0d));
    }

    @Test(expected = InvalidParamValueError.class)
    public void depositIntoAccountWrongPinCard() throws Exception {
        transactionService.depositIntoAccount(account1.getiBAN(), "0", account1.getPinCode(), 200d);
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
                account1.getPinCard(), "-1", 200d);
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
                "-1", account1.getPinCode(), 200d);
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

        transactionService.transferMoney(tokenAccount1, account1.getiBAN(), account2.getiBAN(), "Piet Pietersen",
                200d, "Geld");
        assertThat(infoService.getBalance(tokenAccount1, account1.getiBAN()).getBalance(), equalTo
                (0d));
        assertThat(infoService.getBalance(tokenAccount2, account2.getiBAN()).getBalance(), equalTo
                (200d));
    }

    @Test(expected = NotAuthorizedError.class)
    public void transferMoneyNotAuthorized() throws Exception {
        transactionService.depositIntoAccount(account1.getiBAN(), account1.getPinCard(), account1.getPinCode(), 200d);

        transactionService.transferMoney(tokenAccount2, account1.getiBAN(), account2.getiBAN(), "Piet Pietersen",
                200d, "Geld");
        assertThat(infoService.getBalance(tokenAccount1, account1.getiBAN()).getBalance(), equalTo
                (200d));
        assertThat(infoService.getBalance(tokenAccount2, account2.getiBAN()).getBalance(), equalTo
                (0d));
    }

    @Test(expected = NotAuthorizedError.class)
    public void transferMoneyUnauthorizedSourceIBAN() throws Exception {
        transactionService.depositIntoAccount(account1.getiBAN(), account1.getPinCard(), account1.getPinCode(), 200d);

        transactionService.transferMoney(tokenAccount1, account2.getiBAN(), account2.getiBAN(), "Piet Pietersen",
                200d, "Geld");
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
        transactionService.transferMoney(tokenAccount1, account1.getiBAN(), account2.getiBAN(), "Piet Pietersen",
                -200d, "Geld");
        assertThat(infoService.getBalance(tokenAccount1, account1.getiBAN()).getBalance(), equalTo
                (200d));
        assertThat(infoService.getBalance(tokenAccount2, account2.getiBAN()).getBalance(), equalTo
                (0d));
    }

    @Test(expected = InvalidParamValueError.class)
    public void transferMoneyNotEnoughBalance() throws Exception {
        transactionService.depositIntoAccount(account1.getiBAN(), account1.getPinCard(), account1.getPinCode(), 200d);

        transactionService.transferMoney(tokenAccount1, account1.getiBAN(), account2.getiBAN(), "Piet Pietersen",
                201d, "Geld");
        assertThat(infoService.getBalance(tokenAccount1, account1.getiBAN()).getBalance(), equalTo
                (200d));
        assertThat(infoService.getBalance(tokenAccount2, account2.getiBAN()).getBalance(), equalTo
                (0d));
    }

}