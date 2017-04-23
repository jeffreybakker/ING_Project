package honours.ing.banq.model;

import javax.persistence.*;
import java.util.List;

/**
 * @author Kevin Witlox
 */
@Entity
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ManyToMany(targetEntity = Customer.class)
    private List<Customer> holders;

    @OneToMany(targetEntity = BankAccount.class)
    private BankAccount bankAccount;

    public Card() {}

    public Card(List<Customer> holders, BankAccount bankAccount) {
        this.holders = holders;
        this.bankAccount = bankAccount;
    }

    public Integer getId() { return id; }

    public void addHolder(Customer holder) {
        holders.add(holder);
    }

    public List<Customer> getHolders() { return holders; }

    public void removeHolder(Customer customer) {
        holders.remove(customer);
    }

    public void setHolders(List<Customer> holders) {
        this.holders = holders;
    }

    public BankAccount getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(BankAccount bankAccount) {
        this.bankAccount = bankAccount;
    }

}
