package honours.ing.banq.info.bean;

import honours.ing.banq.account.BankAccount;

/**
 * Used to represent the balance of a {@link BankAccount}
 *
 * @author Kevin Witlox
 */
public class BalanceBean {

    private Double balance;

    public BalanceBean(BankAccount bankAccount) {
        balance = bankAccount.getBalance();
    }

    public Double getBalance() {
        return balance;
    }
}
