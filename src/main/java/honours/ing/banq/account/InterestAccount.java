package honours.ing.banq.account;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.MappedSuperclass;
import java.math.BigDecimal;

/**
 * @author Jeffrey Bakker
 * @since 7-8-17
 */
@MappedSuperclass
public abstract class InterestAccount extends Account {

    private BigDecimal interest;
    private BigDecimal lowestBalance;

    /**
     * @deprecated empty constructor for the Hibernate ORM
     */
    @Deprecated
    public InterestAccount() {
        super();
    }

    /**
     * Creates a new {@link InterestAccount}.
     * @param account the bank account to which this account belongs
     */
    public InterestAccount(BankAccount account) {
        super(account);

        interest = BigDecimal.ZERO;
        lowestBalance = balance;
    }

    @Override
    public void addBalance(BigDecimal delta) {
        super.addBalance(delta);

        if (balance.compareTo(lowestBalance) < 0) {
            lowestBalance = balance;
        }
    }

    /**
     * Adds interest to the interest that will have to be payed at the end of the month.
     * @param delta the amount of interest to add
     */
    public void addInterest(BigDecimal delta) {
        interest = interest.add(delta);
    }

    /**
     * Returns the amount of interest that will have to be payed at the end of the month.
     * @return the amount of interest
     */
    public BigDecimal getInterest() {
        return interest;
    }

    /**
     * Resets the amount of interest that still has to be cashed in.
     */
    public void resetInterest() {
        interest = new BigDecimal(0.0);
    }

    /**
     * Returns the lowest balance on this account since the last reset.
     * @return the lowest balance
     */
    public BigDecimal getLowestBalance() {
        return lowestBalance;
    }

    /**
     * Resets the lowest balance to the current balance.
     */
    public void resetLowestBalance() {
        lowestBalance = balance;
    }

}
