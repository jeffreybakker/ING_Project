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

    @Test
    public void depositIntoAccount() throws Exception {
        transactionService.depositIntoAccount(account1.iBan, account1.cardNumber, account1.pin, 200.0d);
        assertThat(infoService.getBalance(account1.token, account1.iBan).getBalance(), equalTo
                (200d));
    }

    @Test(expected = InvalidPINError.class)
    public void depositIntoAccountWrongPinCode() throws Exception {
        transactionService.depositIntoAccount(account1.iBan, account1.cardNumber, "9999", 200.0d);
        assertThat(infoService.getBalance(account1.token, account1.iBan).getBalance(), equalTo
                (0d));
    }

    @Test(expected = InvalidParamValueError.class)
    public void depositIntoAccountNegativeAmount() throws Exception {
        transactionService.depositIntoAccount(account1.iBan, account1.cardNumber, account1.pin, -200d);
        assertThat(infoService.getBalance(account1.token, account1.iBan).getBalance(), equalTo
                (0d));
    }

    @Test(expected = InvalidParamValueError.class)
    public void depositIntoAccountWrongIBAN() throws Exception {
        transactionService.depositIntoAccount(account2.iBan, account1.cardNumber, account1.pin, 200d);
        assertThat(infoService.getBalance(account1.token, account1.iBan).getBalance(), equalTo
                (0d));
    }

    @Test(expected = InvalidParamValueError.class)
    public void depositIntoAccountWrongIBANType() throws Exception {
        transactionService.depositIntoAccount("null", account1.cardNumber, account1.pin, 200d);
        assertThat(infoService.getBalance(account1.token, account1.iBan).getBalance(), equalTo
                (0d));
    }

    @Test(expected = InvalidParamValueError.class)
    public void depositIntoAccountWrongPinCard() throws Exception {
        transactionService.depositIntoAccount(account1.iBan, "0", account1.pin, 200d);
        assertThat(infoService.getBalance(account1.token, account1.iBan).getBalance(), equalTo
                (0d));
    }


    @Test
    public void payFromAccount() throws Exception {
        // Make sure source iBan has balance
        transactionService.depositIntoAccount(account1.iBan, account1.cardNumber,
                account1.pin, 200d);

        // Transaction
        transactionService.payFromAccount(account1.iBan, account2.iBan,
                account1.cardNumber, account1.pin, 200d);
        assertThat(infoService.getBalance(account1.token, account1.iBan).getBalance(), equalTo
                (0d));
        assertThat(infoService.getBalance(account2.token, account2.iBan).getBalance(), equalTo
                (200d));
    }

    @Test(expected = InvalidPINError.class)
    public void payFromAccountWrongPinCode() throws Exception {
        // Make sure source iBan has balance
        transactionService.depositIntoAccount(account1.iBan, account1.cardNumber,
                account1.pin, 200d);

        // Transaction
        transactionService.payFromAccount(account1.iBan, account2.iBan,
                account1.cardNumber, "-1", 200d);
        assertThat(infoService.getBalance(account1.token, account1.iBan).getBalance(), equalTo
                (200d));
        assertThat(infoService.getBalance(account2.token, account2.iBan).getBalance(), equalTo
                (0d));
    }

    @Test(expected = InvalidParamValueError.class)
    public void payFromAccountWrongPinCard() throws Exception {
        // Make sure source iBan has balance
        transactionService.depositIntoAccount(account1.iBan, account1.cardNumber,
                account1.pin, 200d);

        // Transaction
        transactionService.payFromAccount(account1.iBan, account2.iBan,
                "-1", account1.pin, 200d);
        assertThat(infoService.getBalance(account1.token, account1.iBan).getBalance(), equalTo
                (200d));
        assertThat(infoService.getBalance(account2.token, account2.iBan).getBalance(), equalTo
                (0d));
    }

    @Test(expected = InvalidParamValueError.class)
    public void payFromAccountWrongSourceiBan() throws Exception {
        // Make sure source iBan has balance
        transactionService.depositIntoAccount(account1.iBan, account1.cardNumber,
                account1.pin, 200d);

        // Transaction
        transactionService.payFromAccount("null", account2.iBan,
                account1.cardNumber, account1.pin, 200d);
        assertThat(infoService.getBalance(account1.token, account1.iBan).getBalance(), equalTo
                (200d));
    }

    @Test(expected = InvalidParamValueError.class)
    public void payFromAccountWrongTargetiBan() throws Exception {
        // Make sure source iBan has balance
        transactionService.depositIntoAccount(account1.iBan, account1.cardNumber,
                account1.pin, 200d);

        // Transaction
        transactionService.payFromAccount(account1.iBan, "null",
                account1.cardNumber, account1.pin, 200d);
        assertThat(infoService.getBalance(account1.token, account1.iBan).getBalance(), equalTo
                (200d));
        assertThat(infoService.getBalance(account2.token, account2.iBan).getBalance(), equalTo
                (0d));
    }

    @Test(expected = InvalidParamValueError.class)
    public void payFromAccountNotEnoughBalance() throws Exception {
        // Make sure source iBan has balance
        transactionService.depositIntoAccount(account1.iBan, account1.cardNumber,
                account1.pin, 200d);
        assertThat(infoService.getBalance(account1.token, account1.iBan).getBalance(), equalTo
                (200d));

        // Transaction
        transactionService.payFromAccount(account1.iBan, account2.iBan,
                account1.cardNumber, account1.pin, 201d);
        assertThat(infoService.getBalance(account1.token, account1.iBan).getBalance(), equalTo
                (200d));
        assertThat(infoService.getBalance(account2.token, account2.iBan).getBalance(), equalTo
                (0d));
    }

    @Test(expected = InvalidParamValueError.class)
    public void payFromAccountNegativeAmount() throws Exception {
        // Make sure source iBan has balance
        transactionService.depositIntoAccount(account1.iBan, account1.cardNumber,
                account1.pin, 200d);
        assertThat(infoService.getBalance(account1.token, account1.iBan).getBalance(), equalTo
                (200d));

        // Transaction
        transactionService.payFromAccount(account1.iBan, account2.iBan,
                account1.cardNumber, account1.pin, -200d);
        assertThat(infoService.getBalance(account1.token, account1.iBan).getBalance(), equalTo
                (200d));
        assertThat(infoService.getBalance(account2.token, account2.iBan).getBalance(), equalTo
                (0d));
    }

    @Test
    public void transferMoney() throws Exception {
        transactionService.depositIntoAccount(account1.iBan, account1.cardNumber, account1.pin, 200d);

        transactionService.transferMoney(account1.token, account1.iBan, account2.iBan, "Piet Pietersen",
                200d, "Geld");
        assertThat(infoService.getBalance(account1.token, account1.iBan).getBalance(), equalTo
                (0d));
        assertThat(infoService.getBalance(account2.token, account2.iBan).getBalance(), equalTo
                (200d));
    }

    @Test(expected = NotAuthorizedError.class)
    public void transferMoneyNotAuthorized() throws Exception {
        transactionService.depositIntoAccount(account1.iBan, account1.cardNumber, account1.pin, 200d);

        transactionService.transferMoney(account2.token, account1.iBan, account2.iBan, "Piet Pietersen",
                200d, "Geld");
        assertThat(infoService.getBalance(account1.token, account1.iBan).getBalance(), equalTo
                (200d));
        assertThat(infoService.getBalance(account2.token, account2.iBan).getBalance(), equalTo
                (0d));
    }

    @Test(expected = NotAuthorizedError.class)
    public void transferMoneyUnauthorizedSourceiBan() throws Exception {
        transactionService.depositIntoAccount(account1.iBan, account1.cardNumber, account1.pin, 200d);

        transactionService.transferMoney(account1.token, account2.iBan, account2.iBan, "Piet Pietersen",
                200d, "Geld");
        assertThat(infoService.getBalance(account1.token, account1.iBan).getBalance(), equalTo
                (200d));
        assertThat(infoService.getBalance(account2.token, account2.iBan).getBalance(), equalTo
                (0d));
    }

    @Test(expected = InvalidParamValueError.class)
    public void transferMoneyNonExistantTargetiBan() throws Exception

    {
        transactionService.depositIntoAccount(account1.iBan, account1.cardNumber, account1.pin, 200d);

        transactionService.transferMoney(account1.token, account1.iBan, "null", "Piet Pietersen", 200d, "Geld");
        assertThat(infoService.getBalance(account1.token, account1.iBan).getBalance(), equalTo
                (200d));
        assertThat(infoService.getBalance(account2.token, account2.iBan).getBalance(), equalTo
                (0d));
    }

    @Test(expected = InvalidParamValueError.class)
    public void transferMoneyNegativeBalance() throws Exception {
        transactionService.transferMoney(account1.token, account1.iBan, account2.iBan, "Piet Pietersen",
                -200d, "Geld");
        assertThat(infoService.getBalance(account1.token, account1.iBan).getBalance(), equalTo
                (200d));
        assertThat(infoService.getBalance(account2.token, account2.iBan).getBalance(), equalTo
                (0d));
    }

    @Test(expected = InvalidParamValueError.class)
    public void transferMoneyNotEnoughBalance() throws Exception {
        transactionService.depositIntoAccount(account1.iBan, account1.cardNumber, account1.pin, 200d);

        transactionService.transferMoney(account1.token, account1.iBan, account2.iBan, "Piet Pietersen",
                201d, "Geld");
        assertThat(infoService.getBalance(account1.token, account1.iBan).getBalance(), equalTo
                (200d));
        assertThat(infoService.getBalance(account2.token, account2.iBan).getBalance(), equalTo
                (0d));
    }

}