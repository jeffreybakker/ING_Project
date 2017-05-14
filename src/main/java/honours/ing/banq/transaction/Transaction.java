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

    private String source;
    private String destination;
    private Double amount;

    private String description;
    private Date date;

    /**
     * An empty constructor for the spring framework.
     * @deprecated
     */
    public Transaction() {}

    /**
     * Creates a <code>Transaction</code> with the given parameters.
     * @param source the source account
     * @param destination the destination account
     * @param date the date and time of the transaction, can be just <code>Calendar.getInstance().getTime()</code>
     */
    public Transaction(String source, String destination, Double amount, String description, Date date) {
        this.source = source;
        this.destination = destination;
        this.amount = amount;
        this.description = description;
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
    public String getSource() {
        return source;
    }

    /**
     * Returns the destination account of the transaction.
     * @return the destination account of the transaction
     */
    public String getDestination() {
        return destination;
    }

    public Double getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }

    /**
     * Returns the date and time at which the transaction was made.
     * @return the date of the transaction
     */
    public Date getDate() {
        return date;
    }
}
