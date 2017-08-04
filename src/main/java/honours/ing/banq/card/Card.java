package honours.ing.banq.card;

import honours.ing.banq.account.BankAccount;
import honours.ing.banq.customer.Customer;
import honours.ing.banq.time.TimeUtil;

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
    private String pin; // TODO: add hashing
    private Date expirationDate;

    private int failedAttempts;

    /**
     * @deprecated empty constructor for spring
     */
    public Card() { }

    public Card(Customer holder, BankAccount account, String cardNumber) {
        this.holder = holder;
        this.account = account;
        this.cardNumber = cardNumber;

        pin = generatePin();

        Calendar expiration = Calendar.getInstance();
        expiration.setTimeInMillis(TimeUtil.currentTimeMillis() + DURABILITY);
        expirationDate = expiration.getTime();

        failedAttempts = 0;
    }

    public Integer getId() {
        return id;
    }

    public Customer getHolder() {
        return holder;
    }

    public BankAccount getAccount() {
        return account;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public String getPin() {
        return pin;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public int getFailedAttempts() {
        return failedAttempts;
    }

    public boolean isBlocked() {
        return failedAttempts >= PIN_ATTEMPTS_BEFORE_BLOCK;
    }

    public void addFailedAttempt() {
        failedAttempts++;
    }

    public void resetAttempts() {
        failedAttempts = 0;
    }

    private static String generatePin() {
        StringBuilder res = new StringBuilder();
        Random rnd = new Random();

        for (int i = 0; i < 4; i++) {
            res.append(rnd.nextInt(10));
        }

        return res.toString();
    }

}
