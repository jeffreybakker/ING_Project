package honours.ing.banq.model;

import honours.ing.banq.database.annotation.DBNotNull;

import java.sql.ResultSet;

/**
 * @author Jeffrey Bakker
 */
public abstract class Account extends Model {

    @DBNotNull
    private String name;

    @DBNotNull
    private Money balance;

    @DBNotNull
    private User user;

    /**
     * Creates a new <code>Account</code> without initializing any variables.
     */
    private Account() { }

    /**
     * Creates a new <code>Account</code> with the given arguments.
     * @param name the name of the account (can be <code>null</code>)
     * @param user a reference to the user who owns this account
     * @param balance the initial balance for this account
     */
    Account(String name, User user, Money balance) {
        this.name = name;
        this.user = user;
        this.balance = balance;

        id = genID();
    }

    /**
     * Deposit money into the account.
     * @param amount The amount to deposit
     */
    public void deposit(Money amount) {
        // TODO: Implement
    }

    /**
     * Withdrawal money from the account.
     * @param amount The amount to withdraw
     */
    public void withdraw(Money amount) {
        // TODO: Implement
    }

    /**
     * Parses an <code>Account</code> object from the given SQL <code>ResultSet</code>.
     * @param raw the raw data
     * @return the resulting <code>Account</code> object
     */
    public static Account parseRaw(ResultSet raw) {
        // TODO: Implement
        return null;
    }

    /**
     * Returns the balance on this account.
     * @return the balance as a <code>Money</code> object
     */
    public Money getBalance() {
        return balance;
    }

    /**
     * Returns the name of the account.
     * @return the name of the account
     */
    public String getName() {
        return name;
    }

}
