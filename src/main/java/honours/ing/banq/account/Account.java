package honours.ing.banq.account;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * @author Jeffrey Bakker
 * @since 7-8-17
 */
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @OneToOne
    protected BankAccount account;

    protected BigDecimal balance;

    /**
     * @deprecated empty constructor for the Hibernate ORM
     */
    @Deprecated
    public Account() { }

    /**
     * Creates a new {@link Account}.
     * @param account the bank account to which this account belongs
     */
    public Account(BankAccount account) {
        this.balance = BigDecimal.ZERO;
    }

    /**
     * Returns whether the given amount can be payed from the balance on this account.
     * @param amount the amount the user wishes to pay
     * @return {@code true} if the user can pay the amount
     */
    public boolean canPayAmount(BigDecimal amount) {
        return balance.subtract(amount).compareTo(BigDecimal.ZERO) >= 0;
    }

    /**
     * Returns the {@link BankAccount} to which this account belongs.
     * @return a {@link BankAccount}
     */
    public BankAccount getAccount() {
        return account;
    }

    /**
     * Adds balance to the balance of the account.
     * @param delta the difference in balance
     */
    public void addBalance(BigDecimal delta) {
        balance = balance.add(delta);
    }

    /**
     * Returns the balance on the account.
     * @return the balance
     */
    public BigDecimal getBalance() {
        return balance;
    }

    /**
     * Returns the ID of the account.
     * @return the ID
     */
    public Integer getId() {
        return id;
    }

}
