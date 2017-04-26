package honours.ing.banq.account;

import honours.ing.banq.customer.Customer;

import javax.persistence.*;
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

    @ManyToMany(targetEntity = Customer.class)
    private List<Customer> holders;

    /**
     * An empty constructor for spring.
     * @deprecated
     */
    public BankAccount() { }

    /**
     * Creates a new <code>BankAccount</code> with the given parameters.
     * @param holders a list with the customers that are the holders of the account
     * @param balance the initial balance of the account
     */
    public BankAccount(List<Customer> holders, Double balance) {
        this.holders = holders;
        this.balance = balance;
    }

    /**
     * Returns the id of the account.
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * Adds a holder to the list of holders.
     * @param holder an extra holder
     */
    public void addHolder(Customer holder) {
        holders.add(holder);
    }

    /**
     * Returns the list of holders that can access the account.
     * @return a list of holders
     */
    public List<Customer> getHolders() {
        return holders;
    }

    /**
     * Removes a holder from the account.
     * @param holder the holder to be removed
     */
    public void removeHolder(Customer holder) {
        holders.remove(holder);
    }

    /**
     * Sets the list of holders that can access the account and overrides the old list of holders.
     * @param holders the new list of holders
     */
    public void setHolders(List<Customer> holders) {
        this.holders = holders;
    }

    /**
     * Returns the balance of the account.
     * @return the balance
     */
    public Double getBalance() {
        return balance;
    }

    /**
     * Sets the balance of the account.
     * @param balance the balance
     */
    public void setBalance(Double balance) {
        this.balance = ((double) Math.round(balance * 100.0)) / 100.0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BankAccount that = (BankAccount) o;

        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
