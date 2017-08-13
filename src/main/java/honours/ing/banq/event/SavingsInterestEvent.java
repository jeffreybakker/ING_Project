package honours.ing.banq.event;

import honours.ing.banq.account.model.BankAccount;
import honours.ing.banq.account.BankAccountRepository;
import honours.ing.banq.account.model.SavingAccount;
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
public class SavingsInterestEvent extends InterestEvent {

    private static final InterestRate[] RATES = {
            new InterestRate(new BigDecimal("0.0015"), new BigDecimal("25000")),
            new InterestRate(new BigDecimal("0.0015"), new BigDecimal("75000")),
            new InterestRate(new BigDecimal("0.0020"), new BigDecimal("1000000")),
    };

    // Services
    private TransactionService transactionService;

    // Repositories
    private BankAccountRepository accountRepository;

    @Autowired
    public SavingsInterestEvent(TransactionService transactionService, BankAccountRepository accountRepository) {
        this.transactionService = transactionService;
        this.accountRepository = accountRepository;
    }

    @Override
    public void execute(long time) {
        final Calendar c = Calendar.getInstance();
        c.setTime(new Date(time));

        //noinspection MagicConstant
        final boolean firstOfYear = c.get(DAY_OF_MONTH) == 1 && c.get(MONTH) == 0;
        final InterestRate[] rates = calcDailyRates(c, RATES);

        List<BankAccount> accounts = accountRepository.findAll();

        for (BankAccount account : accounts) {
            SavingAccount sa = account.getSavingAccount();
            if (sa == null) {
                continue;
            }

            BigDecimal balance = sa.getLowestBalance();
            BigDecimal lastMax = new BigDecimal("0.00");
            BigDecimal interest = new BigDecimal("0.00");

            for (InterestRate rate : rates) {
                balance = balance.subtract(lastMax);
                if (balance.compareTo(BigDecimal.ZERO) <= 0) {
                    break;
                }

                BigDecimal delta = rate.maxBalance.subtract(lastMax);
                lastMax = rate.maxBalance;

                BigDecimal rateAmt = balance.min(delta);
                interest = interest.add(rateAmt.multiply(rate.interest));
            }

            sa.addInterest(interest);
            sa.resetLowestBalance();

            if (firstOfYear) {
                transactionService.forceTransactionAccount(
                        sa, sa.getInterest()
                                .setScale(2, BigDecimal.ROUND_HALF_UP), "Interest");
                sa.resetInterest();
            }

            accountRepository.save(account);
        }
    }

    private static InterestRate[] calcDailyRates(Calendar now, InterestRate[] yearlyRates) {
        InterestRate[] res = new InterestRate[yearlyRates.length];

        final int daysInYear = now.getActualMaximum(DAY_OF_YEAR);

        for (int i = 0; i < res.length; i++) {
            res[i] = new InterestRate(
                    yearlyRates[i].interest.divide(new BigDecimal((double) daysInYear), 7, RoundingMode.HALF_UP),
                    yearlyRates[i].maxBalance
            );
        }

        return res;
    }

    private static class InterestRate {

        public BigDecimal interest;
        public BigDecimal maxBalance;

        public InterestRate(BigDecimal interest, BigDecimal maxBalance) {
            this.interest = interest;
            this.maxBalance = maxBalance;
        }
    }

}
