package honours.ing.banq.info.bean;

import honours.ing.banq.account.BankAccount;

/**
 * Used to represent the balance of a {@link BankAccount}
 *
 * @author Kevin Witlox
 */
public class BalanceBean {

    private Double balance;
    private Double savingsAccountBalance;

    public BalanceBean(BankAccount bankAccount) {
        balance = bankAccount.getCheckingAccount().getBalance().doubleValue();
        savingsAccountBalance = bankAccount.getSavingAccount() == null
                ? 0.0d : bankAccount.getSavingAccount().getBalance().doubleValue();
    }

    public Double getBalance() {
        return balance;
    }

    public Double getSavingsAccountBalance() {
        return savingsAccountBalance;
    }
}
