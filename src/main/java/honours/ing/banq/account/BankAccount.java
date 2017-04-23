package honours.ing.banq.account;

import honours.ing.banq.customer.Customer;

import javax.persistence.*;
import java.util.List;

/**
 * @author Jeffrey Bakker
 */
@Entity
public class BankAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private Double balance;

    @ManyToMany(targetEntity = Customer.class)
    private List<Customer> holders;

    public BankAccount() { }

    public BankAccount(List<Customer> holders, Double balance) {
        this.holders = holders;
        this.balance = balance;
    }

    public Integer getId() {
        return id;
    }

    public void addHolder(Customer holder) {
        holders.add(holder);
    }

    public List<Customer> getHolders() {
        return holders;
    }

    public void removeHolder(Customer holder) {
        holders.remove(holder);
    }

    public void setHolders(List<Customer> holders) {
        this.holders = holders;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }
}
