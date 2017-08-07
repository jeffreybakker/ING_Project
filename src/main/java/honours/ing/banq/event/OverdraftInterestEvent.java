package honours.ing.banq.event;

import honours.ing.banq.account.BankAccount;
import honours.ing.banq.account.BankAccountRepository;
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
 * @author Jeffrey Bakker
 * @since 5-8-17
 */
@Component
public class OverdraftInterestEvent implements Event {

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
            if (account.getCheckingAccount().getLowestBalance().compareTo(ZERO) >= 0) {
                continue;
            }

            account.getCheckingAccount().addInterest(account.getCheckingAccount().getLowestBalance().multiply(interest).multiply(new BigDecimal(-1.0)));
            account.getCheckingAccount().resetLowestBalance();

            if (firstOfMonth) {
                transactionService.forceTransactionAccount(
                        account.getCheckingAccount(), account.getCheckingAccount().getInterest()
                                .multiply(new BigDecimal("-1.0"))
                                .setScale(2, BigDecimal.ROUND_HALF_UP), "Interest");
                account.getCheckingAccount().resetInterest();
            }

            accountRepository.save(account);
        }
    }

    @Override
    public long nextIteration(long lastIteration) {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date(lastIteration));

        c.set(HOUR_OF_DAY, 0);
        c.set(MINUTE, 0);
        c.set(SECOND, 0);
        c.set(MILLISECOND, 0);

        c.add(DAY_OF_MONTH, 1);
        return c.getTimeInMillis();
    }

}
