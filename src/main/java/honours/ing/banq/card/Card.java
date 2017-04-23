package honours.ing.banq.card;

import honours.ing.banq.account.BankAccount;
import honours.ing.banq.customer.Customer;

import javax.persistence.*;

/**
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

    public Card() {}

    public Card(Customer holder, BankAccount bankAccount) {
        this.holder = holder;
        this.account = bankAccount;
    }

    public Integer getId() { return id; }

    public BankAccount getAccount() {
        return account;
    }

    public void setAccount(BankAccount account) {
        this.account = account;
    }

    public Customer getHolder() {
        return holder;
    }

    public void setHolder(Customer holder) {
        this.holder = holder;
    }
}
