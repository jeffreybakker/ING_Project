package honours.ing.banq.account;

import honours.ing.banq.customer.Customer;

import javax.persistence.*;
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

    private Double balance;

    @ManyToOne(targetEntity = Customer.class)
    private Customer primaryHolder;

    @ManyToMany(targetEntity = Customer.class)
    private List<Customer> holders;

    /**
     * @deprecated empty constructor for spring
     */
    public BankAccount() {
    }

    /**
     * Creates a new {@link BankAccount} with the given primary holder and 0.0 for balance.
     * @param primaryHolder
     */
    public BankAccount(Customer primaryHolder) {
        this.primaryHolder = primaryHolder;
        balance = 0.0;
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
    public Double getBalance() {
        return balance;
    }

    /**
     * Subtracts balance from the account.
     * @param balance the difference in balance
     */
    public void subBalance(Double balance) {
        this.balance -= balance;
    }

    /**
     * Adds balance on the account.
     * @param balance the difference in balance
     */
    public void addBalance(Double balance) {
        this.balance += balance;
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
