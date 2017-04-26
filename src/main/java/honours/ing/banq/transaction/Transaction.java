package honours.ing.banq.transaction;

import honours.ing.banq.account.BankAccount;

import javax.persistence.*;
import java.util.Date;

/**
 * Represents a transaction that can be made between two bank accounts.
 * @author Jeffrey Bakker
 * @since 24-4-17
 */
@Entity
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @OneToOne(targetEntity = BankAccount.class)
    private BankAccount source;
    private Double newSourceBalance;

    @OneToOne(targetEntity = BankAccount.class)
    private BankAccount destination;
    private Double newDestinationBalance;

    private Date date;

    /**
     * An empty constructor for the spring framework.
     * @deprecated
     */
    public Transaction() {}

    /**
     * Creates a <code>Transaction</code> with the given parameters.
     * @param source the source account
     * @param newSourceBalance the new balance of the source account
     * @param destination the destination account
     * @param newDestinationBalance the new balance of the destination account
     * @param date the date and time of the transaction, can be just <code>Calendar.getInstance().getTime()</code>
     */
    public Transaction(BankAccount source, Double newSourceBalance, BankAccount destination, Double newDestinationBalance, Date date) {
        this.source = source;
        this.newSourceBalance = newSourceBalance;
        this.destination = destination;
        this.newDestinationBalance = newDestinationBalance;
        this.date = date;
    }

    /**
     * Returns the id of the transaction.
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * Returns the source account of the transaction
     * @return the source account
     */
    public BankAccount getSource() {
        return source;
    }

    /**
     * Returns the new balance of the source account.
     * @return the new balance of the source account
     */
    public Double getNewSourceBalance() {
        return newSourceBalance;
    }

    /**
     * Returns the destination account of the transaction.
     * @return the destination account of the transaction
     */
    public BankAccount getDestination() {
        return destination;
    }

    /**
     * Returns the new balance of the destination account.
     * @return the new balance of the destination account
     */
    public Double getNewDestinationBalance() {
        return newDestinationBalance;
    }

    /**
     * Returns the date and time at which the transaction was made.
     * @return the date of the transaction
     */
    public Date getDate() {
        return date;
    }
}
