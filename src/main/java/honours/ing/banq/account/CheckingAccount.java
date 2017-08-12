package honours.ing.banq.account;

import javax.persistence.Entity;
import java.math.BigDecimal;

/**
 * An regular checking account that can be used for paying.
 * @author Jeffrey Bakker
 * @since 7-8-17
 */
@Entity
public class CheckingAccount extends InterestAccount {

    private static final BigDecimal MINUS_ONE = new BigDecimal("-1.0");

    private BigDecimal overdraftLimit;

    /**
     * @deprecated empty constructor for the Hibernate ORM.
     */
    @Deprecated
    public CheckingAccount() {
        super();
    }

    /**
     * Creates a new {@link CheckingAccount}.
     * @param account the bank account to which this account belongs
     */
    public CheckingAccount(BankAccount account) {
        super(account);

        overdraftLimit = BigDecimal.ZERO;
    }

    @Override
    public boolean canPayAmount(BigDecimal amount) {
        return balance.subtract(amount).compareTo(overdraftLimit) >= 0;
    }

    /**
     * Returns the maximum amount of negative balance that this account may have.
     * @return the overdraft limit
     */
    public BigDecimal getOverdraftLimit() {
        return overdraftLimit.multiply(MINUS_ONE);
    }

    /**
     * Sets the maximum amount of negative balance that this account may have.
     * @param overdraftLimit the overdraft limit
     */
    public void setOverdraftLimit(BigDecimal overdraftLimit) {
        this.overdraftLimit = overdraftLimit.multiply(MINUS_ONE);
    }

}
