package honours.ing.banq.account.bean;

import java.math.BigDecimal;

/**
 * @author jeffrey
 * @since 5-8-17
 */
public class OverdraftBean {

    private double overdraftLimit;

    public OverdraftBean(BigDecimal overdraftLimit) {
        this.overdraftLimit = overdraftLimit.doubleValue();
    }

    public double getOverdraftLimit() {
        return overdraftLimit;
    }

    public void setOverdraftLimit(double overdraftLimit) {
        this.overdraftLimit = overdraftLimit;
    }
}
