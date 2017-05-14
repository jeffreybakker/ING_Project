package honours.ing.banq.account;

import com.fasterxml.jackson.annotation.JsonView;
import honours.ing.banq.View;
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

    @JsonView(View.OpenAccountView.class)
    @Column(unique = true)
    private String iban;

    private Double balance;

    @ManyToOne(targetEntity = Customer.class)
    private Customer primaryHolder;

    @ManyToMany(targetEntity = Customer.class)
    private List<Customer> holders;

    /**
     * An empty constructor for spring.
     * @deprecated
     */
    public BankAccount() { }

    /**
     * Creates a new <code>BankAccount</code> with the given parameters.
     * @param iban the IBAN of the bank account
     * @param primaryHolder the primary holder of the bank account
     */
    public BankAccount(String iban, Customer primaryHolder) {
        this.iban = iban;
        this.primaryHolder = primaryHolder;

        holders = new ArrayList<>();
        balance = 0.0;
    }

    /**
     * Returns the id of the account.
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    public String getIban() {
        return iban;
    }

    public Customer getPrimaryHolder() {
        return primaryHolder;
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
