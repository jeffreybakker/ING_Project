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

    @ManyToOne(targetEntity = Customer.class)
    private Customer primaryHolder;

    @ManyToMany(targetEntity = Customer.class)
    private List<Customer> holders;

    @OneToOne(targetEntity = CheckingAccount.class, cascade = CascadeType.ALL, orphanRemoval = true)
    private CheckingAccount checkingAccount;

    @OneToOne(targetEntity = SavingAccount.class, cascade = CascadeType.ALL, orphanRemoval = true)
    private SavingAccount savingAccount;

    /**
     * @deprecated empty constructor for spring
     */
    @Deprecated
    public BankAccount() { }

    /**
     * Creates a new {@link BankAccount} with the given primary holder and 0.0 for balance.
     * @param primaryHolder the primary holder of this account
     */
    public BankAccount(Customer primaryHolder) {
        this.primaryHolder = primaryHolder;
        holders = new ArrayList<>();

        checkingAccount = new CheckingAccount(this);
    }

    /**
     * Returns this {@link BankAccount}'s {@link CheckingAccount}.
     * @return a {@link CheckingAccount}
     */
    public CheckingAccount getCheckingAccount() {
        return checkingAccount;
    }

    /**
     * Adds a {@link SavingAccount} to this {@link BankAccount}.
     */
    public void addSavingAccount() {
        savingAccount = new SavingAccount(this);
    }

    /**
     * Returns this {@link BankAccount}'s {@link SavingAccount}.
     * @return a {@link SavingAccount}
     */
    public SavingAccount getSavingAccount() {
        return savingAccount;
    }

    /**
     * Removes this {@link BankAccount}'s {@link SavingAccount}.
     */
    public void removeSavingAccount() {
        savingAccount = null;
    }

    /**
     * Returns the ID of the account.
     * @return the ID
     */
    public Integer getId() {
        return id;
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
