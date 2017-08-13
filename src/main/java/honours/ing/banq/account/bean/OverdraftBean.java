package honours.ing.banq.account.bean;

import java.math.BigDecimal;

/**
 * This bean is used as a return type for some service's method(s).
 * @author Jeffrey Bakker
 * @since 5-8-17
 */
public class OverdraftBean {

    private double overdraftLimit;

    /**
     * Creates a new {@link OverdraftBean} with the given parameters.
     * @param overdraftLimit the overdraft limit for a bank account
     */
    public OverdraftBean(BigDecimal overdraftLimit) {
        this.overdraftLimit = overdraftLimit.doubleValue();
    }

    /**
     * Returns the overdraft limit.
     * @return the overdraft limit
     */
    public double getOverdraftLimit() {
        return overdraftLimit;
    }

    /**
     * Sets the overdraft limit.
     * @param overdraftLimit the overdraft limit
     */
    public void setOverdraftLimit(double overdraftLimit) {
        this.overdraftLimit = overdraftLimit;
    }
}
