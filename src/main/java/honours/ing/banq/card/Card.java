package honours.ing.banq.card;

import honours.ing.banq.account.BankAccount;
import honours.ing.banq.customer.Customer;

import javax.persistence.*;

/**
 * Represents a physical card that a holder of an bank account can use to make pin transactions.
 * @author Kevin Witlox
 */
@Entity
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ManyToOne(targetEntity = Customer.class)
    private Customer holder;

    @ManyToOne(targetEntity = BankAccount.class)
    private BankAccount account;

    /**
     * An empty constructor for the spring framework.
     * @deprecated
     */
    public Card() {}

    /**
     * Creates a new <code>Card</code> with the given parameters.
     * @param holder the holder of the card
     * @param bankAccount the account corresponding to the card
     */
    public Card(Customer holder, BankAccount bankAccount) {
        this.holder = holder;
        this.account = bankAccount;
    }

    /**
     * Returns the id of the card.
     * @return the id
     */
    public Integer getId() { return id; }

    /**
     * Returns the bank account of the card.
     * @return the bank account
     */
    public BankAccount getAccount() {
        return account;
    }

    /**
     * Sets the bank account of the card.
     * @param account the bank account
     */
    public void setAccount(BankAccount account) {
        this.account = account;
    }

    /**
     * Returns the customer that is the holder of the card.
     * @return the holder
     */
    public Customer getHolder() {
        return holder;
    }

    /**
     * Sets the holder of the card.
     * @param holder the holder
     */
    public void setHolder(Customer holder) {
        this.holder = holder;
    }
}