package honours.ing.banq.card;

import honours.ing.banq.account.BankAccount;
import honours.ing.banq.customer.Customer;

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
    private Integer cardNumber;
    private Integer pin; // TODO: add hashing
    private Date expirationDate;

    /**
     * @deprecated empty constructor for spring
     */
    public Card() { }

    public Card(Customer holder, BankAccount account, Integer cardNumber) {
        this.holder = holder;
        this.account = account;
        this.cardNumber = cardNumber;

        pin = new Random().nextInt(10000); // 4 number PIN

        Calendar expiration = Calendar.getInstance();
        expiration.setTimeInMillis(System.currentTimeMillis() + DURABILITY);
        expirationDate = expiration.getTime();
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

    public Integer getCardNumber() {
        return cardNumber;
    }

    public Integer getPin() {
        return pin;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }
}
