package honours.ing.banq.account;

import honours.ing.banq.customer.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Interacts with the <code>BankAccount</code> table of the database.
 * @author Jeffrey Bakker
 */
public interface BankAccountRepository extends JpaRepository<BankAccount, Integer> {

    /**
     * Returns a list of all bank accounts of which the customer is a secondary holder.
     * @param customer the customer's ID
     * @return a list of bank accounts
     */
    @Query("SELECT account FROM BankAccount account JOIN account.holders holder WHERE holder.id = ?1")
    List<BankAccount> findBankAccountsByHolders(int customer);

    /**
     * Returns a list of all bank accounts of which the customer is a primary holder.
     * @param customer the customer's ID
     * @return a list of bank accounts
     */
    @Query("SELECT account FROM BankAccount account WHERE account.primaryHolder.id = ?1")
    List<BankAccount> findBankAccountsByPrimaryHolder(int customer);

}
