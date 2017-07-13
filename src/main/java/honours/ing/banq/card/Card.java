package honours.ing.banq.card;

import honours.ing.banq.account.BankAccount;
import honours.ing.banq.customer.Customer;

import javax.persistence.*;
import java.security.SecureRandom;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

/**
 * Represents a physical card that a holder of an bank account can use to make pin transactions.
 * @author Kevin Witlox
 */
@Entity
public class Card {

    @SuppressWarnings("NumericOverflow")
    public static final long DURABILITY = 1000 * 60 * 60 * 24 * 365 * 5; // 5 years

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

    private boolean invalidated;

    /**
     * @deprecated empty constructor for spring
     */
    public Card() { }

    public Card(String token, Customer holder, BankAccount account, String cardNumber, Date currDate) {
        this.holder = holder;
        this.account = account;
        this.cardNumber = cardNumber;
        this.invalidated = false;

        pin = generatePin(token);

        long milisec = currDate.getTime() + Card.DURABILITY;
        expirationDate = new Date();
        expirationDate.setTime(milisec);
    }

    public Card(String token, Customer holder, BankAccount account, String cardNumber, String pin, Date currDate) {
        this(token, holder, account, cardNumber, currDate);
        this.pin = pin;
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

    public boolean isInvalidated() {
        return invalidated;
    }

    public void invalidate() {
        this.invalidated = true;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    private static String generatePin(String authToken) {
        StringBuilder res = new StringBuilder();
        Random rnd = new SecureRandom((authToken + System.currentTimeMillis()).getBytes());

        for (int i = 0; i < 4; i++) {
            res.append(rnd.nextInt(10));
        }

        return res.toString();
    }

}
