package honours.ing.banq.account;

import javax.persistence.Entity;

/**
 * @author jeffrey
 * @since 7-8-17
 */
@Entity
public class SavingAccount extends InterestAccount {

    /**
     * @deprecated empty constructor for the Hibernate ORM.
     */
    @Deprecated
    public SavingAccount() {
        super();
    }

    /**
     * Creates a new {@link SavingAccount}
     * @param account the bank account to which this account belongs
     */
    public SavingAccount(BankAccount account) {
        super(account);
    }

}
