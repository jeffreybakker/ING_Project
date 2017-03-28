package honours.ing.banq.model;

import javax.persistence.*;

/**
 * @author Jeffrey Bakker
 */
@Entity
public class BankAccount {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;

    @ManyToMany
    private Customer[] holders;

    private Double balance;

    public BankAccount() { }

    public BankAccount(Customer[] holders, Double balance) {
        this.holders = holders;
        this.balance = balance;
    }

    public Integer getId() {
        return id;
    }

    public Customer[] getHolders() {
        return holders;
    }

    public void setHolder(Customer[] holders) {
        this.holders = holders;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }
}
