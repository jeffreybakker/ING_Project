package honours.ing.banq.transaction;

import honours.ing.banq.account.BankAccount;

import javax.persistence.*;
import java.util.Date;

/**
 * @author jeffrey
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

    public Transaction(BankAccount source, Double newSourceBalance, BankAccount destination, Double newDestinationBalance, Date date) {
        this.source = source;
        this.newSourceBalance = newSourceBalance;
        this.destination = destination;
        this.newDestinationBalance = newDestinationBalance;
        this.date = date;
    }

    public Integer getId() {
        return id;
    }

    public BankAccount getSource() {
        return source;
    }

    public Double getNewSourceBalance() {
        return newSourceBalance;
    }

    public BankAccount getDestination() {
        return destination;
    }

    public Double getNewDestinationBalance() {
        return newDestinationBalance;
    }

    public Date getDate() {
        return date;
    }
}
