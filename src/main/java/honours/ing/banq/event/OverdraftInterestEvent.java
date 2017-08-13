package honours.ing.banq.event;

import honours.ing.banq.account.model.BankAccount;
import honours.ing.banq.account.BankAccountRepository;
import honours.ing.banq.account.model.CheckingAccount;
import honours.ing.banq.transaction.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static java.util.Calendar.*;

/**
 * An interest event that calculates interest over the overdraft over checking accounts.
 * @author Jeffrey Bakker
 * @since 5-8-17
 */
@Component
public class OverdraftInterestEvent extends InterestEvent {

    private static final BigDecimal INTEREST = new BigDecimal("0.10");
    private static final BigDecimal MONTHLY_RATE = new BigDecimal(Math.pow(INTEREST.doubleValue() + 1.0, 1.0 / 12.0))
            .subtract(new BigDecimal(1.0));

    private static final BigDecimal ZERO = new BigDecimal(0.0d);

    // Services
    private TransactionService transactionService;

    // Repositories
    private BankAccountRepository accountRepository;

    @Autowired
    public OverdraftInterestEvent(TransactionService transactionService, BankAccountRepository accountRepository) {
        this.transactionService = transactionService;
        this.accountRepository = accountRepository;
    }

    @Override
    public void execute(long time) {
        final Calendar c = Calendar.getInstance();
        c.setTime(new Date(time));

        //noinspection MagicConstant
        final boolean firstOfMonth = c.get(DAY_OF_MONTH) == 1;

        final int daysInMonth = c.getActualMaximum(DAY_OF_MONTH);
        final BigDecimal interest = MONTHLY_RATE.divide(
                new BigDecimal((double) daysInMonth),
                7, RoundingMode.HALF_UP);

        List<BankAccount> accounts = accountRepository.findAll();

        for (BankAccount account : accounts) {
            // Skip this account if its balance is positive
            if (account.getCheckingAccount().getLowestBalance().compareTo(ZERO) >= 0) {
                continue;
            }

            CheckingAccount ca = account.getCheckingAccount();

            // Calculate the interest and add it to the account
            ca.addInterest(ca.getLowestBalance().multiply(interest).multiply(new BigDecimal(-1.0)));
            ca.resetLowestBalance();

            if (firstOfMonth) {
                // If it is the first of the month add let the customer pay the interest that has been summed up over
                // the last month
                transactionService.forceTransactionAccount(
                        ca, ca.getInterest()
                                .multiply(new BigDecimal("-1.0"))
                                .setScale(2, BigDecimal.ROUND_HALF_UP), "Interest");
                ca.resetInterest();
            }

            // Finally save the account
            accountRepository.save(account);
        }
    }

}
