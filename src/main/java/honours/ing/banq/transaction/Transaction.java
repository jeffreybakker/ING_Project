package honours.ing.banq.transaction;

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
    private String targetName;

    private Date date;
    private Double amount;
    private String description;

    /**
     * @deprecated an empty constructor for the Hibernate ORM.
     */
    @Deprecated
    public Transaction() { }

    /**
     * Creates a new transaction with the given parameters.
     * @param source      the source iBAN
     * @param destination the destination iBAN
     * @param targetName  the name of the destination
     * @param date        the date of the transaction
     * @param amount      the amount that was transferred
     * @param description the description of the transaction
     */
    public Transaction(String source, String destination, String targetName, Date date, Double amount, String description) {
        this.source = source;
        this.destination = destination;
        this.targetName = targetName;
        this.date = date;
        this.amount = amount;
        this.description = description;
    }

    /**
     * Returns the ID of the transaction.
     * @return the ID
     */
    public Integer getId() {
        return id;
    }

    /**
     * Returns the source iBAN.
     * @return the source iBAN
     */
    public String getSource() {
        return source;
    }

    /**
     * Returns the destination iBAN.
     * @return the destination iBAN
     */
    public String getDestination() {
        return destination;
    }

    /**
     * Returns a name for the destination account.
     * @return the name for the destination
     */
    public String getTargetName() {
        return targetName;
    }

    /**
     * Returns the date of the transaction.
     * @return the date of the transaction
     */
    public Date getDate() {
        return date;
    }

    /**
     * Returns the amount transferred in this transaction.
     * @return the amount that was transferred
     */
    public Double getAmount() {
        return amount;
    }

    /**
     * Returns the description of this transaction.
     * @return the description
     */
    public String getDescription() {
        return description;
    }
}
