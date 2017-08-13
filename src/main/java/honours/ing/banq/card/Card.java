package honours.ing.banq.card;

import honours.ing.banq.account.model.BankAccount;
import honours.ing.banq.customer.Customer;
import honours.ing.banq.time.TimeServiceImpl;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

/**
 * Represents a physical card that a holder of an bank account can use to make pin transactions.
 * @author Kevin Witlox
 */
@Entity
public class Card {

    private static final int PIN_ATTEMPTS_BEFORE_BLOCK = 3;

    @SuppressWarnings("NumericOverflow")
    private static final long DURABILITY = 1000 * 60 * 60 * 24 * 365 * 5; // 5 years

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ManyToOne(targetEntity = Customer.class)
    private Customer holder;

    @ManyToOne(targetEntity = BankAccount.class)
    private BankAccount account;

    @Column(unique = true, nullable = false)
    private String cardNumber;
    private String pin;

    private Date expirationDate;
    private boolean invalidated;
    private int failedAttempts;

    /**
     * @deprecated empty constructor for spring
     */
    @Deprecated
    public Card() { }

    /**
     * Creates a new {@link Card} with the given parameters.
     * @param holder     the holder of the card
     * @param account    the account the card gives access to
     * @param cardNumber the number of the card
     */
    public Card(Customer holder, BankAccount account, String cardNumber) {
        this(holder, account, cardNumber, generatePin());
    }

    /**
     * Creates a new {@link Card} with the given parameters.
     * @param holder     the holder of the card
     * @param account    the account the card gives access to
     * @param cardNumber the number of the cad
     * @param pin        the pin of the card
     */
    public Card(Customer holder, BankAccount account, String cardNumber, String pin) {
        this.holder = holder;
        this.account = account;
        this.cardNumber = cardNumber;
        this.pin = pin;

        Calendar expiration = Calendar.getInstance();
        expiration.setTimeInMillis(TimeServiceImpl.currentTimeMillis() + DURABILITY);
        expirationDate = expiration.getTime();

        failedAttempts = 0;
        invalidated = false;
    }

    /**
     * Returns the ID of this card.
     * @return the ID
     */
    public Integer getId() {
        return id;
    }

    /**
     * Returns the customer that is the holder of this card.
     * @return the holder
     */
    public Customer getHolder() {
        return holder;
    }

    /**
     * Returns the {@link BankAccount} to which this card provides access.
     * @return the {@link BankAccount}
     */
    public BankAccount getAccount() {
        return account;
    }

    /**
     * Returns the number of the PIN card.
     * @return the PIN card's number
     */
    public String getCardNumber() {
        return cardNumber;
    }

    /**
     * Returns the PIN code of the PIN card.
     * @return the PIN code
     */
    public String getPin() {
        return pin;
    }

    /**
     * Returns whether the card is invalidated.
     * @return {@code true} if the card is invalidated
     */
    public boolean isInvalidated() {
        return invalidated;
    }

    /**
     * Sets whether the card is invalidated or not.
     * @param invalidated {@code true} if the card should be invalidated
     */
    public void setInvalidated(boolean invalidated) {
        this.invalidated = invalidated;
    }

    /**
     * Returns whether the hard has been expired or not.
     * @return {@code true} if the card has expired
     */
    public boolean hasExpired() {
        return expirationDate.getTime() >= TimeServiceImpl.getCurDate().getTime();
    }

    /**
     * Returns the expiration date of this card.
     * @return this card's expiration date
     */
    public Date getExpirationDate() {
        return expirationDate;
    }

    /**
     * Returns the amount of consecutive failed PIN attempts.
     * @return the amount of failed attempts of this card
     */
    public int getFailedAttempts() {
        return failedAttempts;
    }

    /**
     * Returns whether the card is blocked or not.
     * @return {@code true} if the card is blocked
     */
    public boolean isBlocked() {
        return failedAttempts >= PIN_ATTEMPTS_BEFORE_BLOCK;
    }

    /**
     * Adds a failed attempt to the counter.
     */
    public void addFailedAttempt() {
        failedAttempts++;
    }

    /**
     * Resets the amount of failed attempts.
     */
    public void resetAttempts() {
        failedAttempts = 0;
    }

    /**
     * Generates a random PIN code.
     * @return a random PIN code
     */
    private static String generatePin() {
        StringBuilder res = new StringBuilder();
        Random rnd = new Random();

        for (int i = 0; i < 4; i++) {
            res.append(rnd.nextInt(10));
        }

        return res.toString();
    }

}
