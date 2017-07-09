package honours.ing.banq.info;

import honours.ing.banq.BoilerplateTest;
import honours.ing.banq.InvalidParamValueError;
import honours.ing.banq.access.AccessService;
import honours.ing.banq.auth.NotAuthorizedError;
import honours.ing.banq.info.bean.BalanceBean;
import honours.ing.banq.info.bean.UserAccessBean;
import honours.ing.banq.transaction.Transaction;
import honours.ing.banq.transaction.TransactionService;
import honours.ing.banq.util.IBANUtil;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

/**
 * @author Kevin Witlox
 * @since 8-7-2017.
 */
public class InfoServiceTest extends BoilerplateTest {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private AccessService accessService;

    @Test
    public void getBalance() throws Exception {
        BalanceBean balanceBean = infoService.getBalance(tokenAccount1, account1.getiBAN());
        assertThat(balanceBean.getBalance(), equalTo(0d));
    }

    @Test(expected = NotAuthorizedError.class)
    public void getBalanceWrongToken() throws Exception {
        infoService.getBalance(tokenAccount1, account2.getiBAN());
    }

    @Test(expected = NotAuthorizedError.class)
    public void getBalanceInvalidToken() throws Exception {
        infoService.getBalance("-1", account1.getiBAN());
    }

    @Test(expected = InvalidParamValueError.class)
    public void getBalanceInvalidIBAN() throws Exception {
        infoService.getBalance(tokenAccount1, "-1");
    }

    @Test(expected = InvalidParamValueError.class)
    public void getBalanceWrongIBAN() throws Exception {
        infoService.getBalance(tokenAccount1, IBANUtil.generateIBAN(1234));
    }

    @Test
    public void getTransactionsOverview() throws Exception {
        List<Transaction> transactions = infoService.getTransactionsOverview(tokenAccount1, account1.getiBAN(), 2);
        assertThat(transactions.size(), equalTo(0));

        Transaction transaction;

        // Checked:
        // - nrOfTransactions correct
        // - correct number of transactions returned for different accounts

        // Test Transaction 1 (Deposit)
        transactionService.depositIntoAccount(account1.getiBAN(), account1.getPinCard(), account1.getPinCode(), 500d);
        transactions = infoService.getTransactionsOverview(tokenAccount1, account1.getiBAN(), 2);
        assertThat(transactions.size(), equalTo(1));
        transaction = transactions.get(0);
        assertThat(transaction.getAmount(), equalTo(500d));
        assertThat(transaction.getSource(), nullValue());
        assertThat(transaction.getDestination(), equalTo(account1.getiBAN()));

        // Test Transaction 2
        transactionService.transferMoney(tokenAccount1, account1.getiBAN(), account2
                .getiBAN(), "null", 200d, "Test Transaction 2");
        transactions = infoService.getTransactionsOverview(tokenAccount1, account1.getiBAN(), 2);
        assertThat(transactions.size(), equalTo(2));
        transaction = transactions.get(1);
        assertThat(transaction.getAmount(), equalTo(200d));
        assertThat(transaction.getSource(), equalTo(account1.getiBAN()));
        assertThat(transaction.getDestination(), equalTo(account2.getiBAN()));

        // Test Transaction 3
        transactionService.transferMoney(tokenAccount1, account1.getiBAN(), account2
                .getiBAN(), "null", 150d, "Test Transaction 3");
        transactions = infoService.getTransactionsOverview(tokenAccount1, account1.getiBAN(), 2);
        assertThat(transactions.size(), equalTo(2));
        transactions = infoService.getTransactionsOverview(tokenAccount1, account1.getiBAN(), 10);
        assertThat(transactions.size(), equalTo(3));
        transaction = transactions.get(2);
        assertThat(transaction.getAmount(), equalTo(150d));
        assertThat(transaction.getSource(), equalTo(account1.getiBAN()));
        assertThat(transaction.getDestination(), equalTo(account2.getiBAN()));

        // Test Transaction 4
        transactionService.transferMoney(tokenAccount2, account2.getiBAN(), account1
                .getiBAN(), "null", 100d, "Test Transaction 4");
        transactions = infoService.getTransactionsOverview(tokenAccount2, account2.getiBAN(), 10);
        assertThat(transactions.size(), equalTo(3));
        transaction = transactions.get(2);
        assertThat(transaction.getAmount(), equalTo(100d));
        assertThat(transaction.getSource(), equalTo(account2.getiBAN()));
        assertThat(transaction.getDestination(), equalTo(account1.getiBAN()));
    }

    @Test(expected = InvalidParamValueError.class)
    public void getTransactionsOverviewNegativeNrOfTransactions() throws Exception {
        infoService.getTransactionsOverview(tokenAccount1, account1.getiBAN(), -1);
    }

    @Test(expected = NotAuthorizedError.class)
    public void getTransactionsOverviewWrongToken() throws Exception {
        infoService.getTransactionsOverview(tokenAccount1, account2.getiBAN(), 2);
    }

    @Test(expected = NotAuthorizedError.class)
    public void getTransactionsOverviewInvalidToken() throws Exception {
        infoService.getTransactionsOverview("-1", account1.getiBAN(), 2);
    }

    @Test(expected = InvalidParamValueError.class)
    public void getTransactionsOverviewWrongIBAN() throws Exception {
        infoService.getTransactionsOverview(tokenAccount1, IBANUtil.generateIBAN(1234), 2);
    }

    @Test(expected = InvalidParamValueError.class)
    public void getTransactionsOverviewInvalidIBAN() throws Exception {
        infoService.getTransactionsOverview(tokenAccount1, "-1", 2);
    }

    @Test
    public void getUserAccess() throws Exception {
        List<UserAccessBean> userAccessBeans;

        userAccessBeans = infoService.getUserAccess(tokenAccount1);
        assertThat(userAccessBeans.size(), equalTo(1));
        assertThat(userAccessBeans.get(0).getiBAN(), equalTo(account1.getiBAN()));
        assertThat(userAccessBeans.get(0).getOwner(), equalTo("Jan Jansen"));

        // Provide Access
        accessService.provideAccess(tokenAccount2, account2.getiBAN(), "Jan Jansen");
        userAccessBeans = infoService.getUserAccess(tokenAccount1);
        assertThat(userAccessBeans.size(), equalTo(2));

        boolean suc1 = false, suc2 = false;
        for (UserAccessBean userAccessBean : userAccessBeans) {
            if (userAccessBean.getiBAN().equals(account1.getiBAN()) && userAccessBean.getOwner().equals("Jan Jansen")) {
                suc1 = true;
            }

            if (userAccessBean.getiBAN().equals(account2.getiBAN()) && userAccessBean.getOwner().equals("Piet Pietersen")) {
                suc2 = true;
            }
        }

        assertThat(suc1 && suc2, is(true));

        // Revoke Access
        accessService.revokeAccess(tokenAccount2, account2.getiBAN(), "Jan Jansen");
        userAccessBeans = infoService.getUserAccess(tokenAccount1);
        assertThat(userAccessBeans.size(), equalTo(1));
        assertThat(userAccessBeans.get(0).getiBAN(), equalTo(account1.getiBAN()));
        assertThat(userAccessBeans.get(0).getOwner(), equalTo("Jan Jansen"));
    }

    @Test
    public void getBankAccountAccess() throws Exception {

    }

}