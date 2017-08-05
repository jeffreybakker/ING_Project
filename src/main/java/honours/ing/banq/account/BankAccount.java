package honours.ing.banq.account;

import honours.ing.banq.customer.Customer;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a bank account of one or more customers.
 * @author Jeffrey Bakker
 */
@Entity
public class BankAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private BigDecimal balance;
    private BigDecimal interest;
    private BigDecimal lowestBalance;
    private BigDecimal overdraftLimit;

    @ManyToOne(targetEntity = Customer.class)
    private Customer primaryHolder;

    @ManyToMany(targetEntity = Customer.class)
    private List<Customer> holders;

    /**
     * @deprecated empty constructor for spring
     */
    @Deprecated
    public BankAccount() { }

    /**
     * Creates a new {@link BankAccount} with the given primary holder and 0.0 for balance.
     * @param primaryHolder
     */
    public BankAccount(Customer primaryHolder) {
        this.primaryHolder = primaryHolder;
        balance = new BigDecimal(0.0);
        interest = new BigDecimal(0.0);
        lowestBalance = new BigDecimal(0.0);
        overdraftLimit = new BigDecimal(0.0);
        holders = new ArrayList<>();
    }

    /**
     * Returns the ID of the account.
     * @return the ID
     */
    public Integer getId() {
        return id;
    }

    /**
     * Returns the balance of the account.
     * @return the balance
     */
    public BigDecimal getBalance() {
        return balance;
    }

    /**
     * Adds balance on the account.
     * @param delta the difference in balance
     */
    public void addBalance(BigDecimal delta) {
        balance = balance.add(delta);
        if (balance.compareTo(lowestBalance) < 0) {
            lowestBalance = balance;
        }
    }

    /**
     * Returns whether the amount can be payed from this bank account with respect to the overdraft limit.
     * @param amount the amount the user wishes to pay
     * @return {@code true} if the account has enough balance
     */
    public boolean canPayAmount(BigDecimal amount) {
        return balance.subtract(amount).compareTo(overdraftLimit) >= 0;
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

    /**
     * Returns the maximum amount of negative balance that this account may have.
     * @return the overdraft limit
     */
    public BigDecimal getOverdraftLimit() {
        return overdraftLimit.multiply(new BigDecimal(-1.0));
    }

    /**
     * Sets the maximum amount of negative balance that this account may have.
     * @param overdraftLimit the overdraft limit
     */
    public void setOverdraftLimit(BigDecimal overdraftLimit) {
        this.overdraftLimit = overdraftLimit.multiply(new BigDecimal(-1.0));
    }

    /**
     * Returns the primary holder of the account.
     * @return the primary holder
     */
    public Customer getPrimaryHolder() {
        return primaryHolder;
    }

    /**
     * Adds a holder to the list of holders of the account.
     * @param holder the holder to be added
     */
    public void addHolder(Customer holder) {
        holders.add(holder);
    }

    /**
     * Returns a list of holders that have access to the account.
     * @return a list of holders
     */
    public List<Customer> getHolders() {
        return holders;
    }

    /**
     * Removes a holder from this account.
     * @param holder the holder to be removed
     */
    public void removeHolder(Customer holder) {
        holders.remove(holder);
    }

}
