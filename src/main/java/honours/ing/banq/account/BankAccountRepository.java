package honours.ing.banq.account;

import honours.ing.banq.customer.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Interacts with the <code>BankAccount</code> table of the database.
 *
 * @author Jeffrey Bakker
 */
public interface BankAccountRepository extends JpaRepository<BankAccount, Integer> {

    @Query("SELECT account FROM BankAccount account JOIN account.holders holder WHERE holder.id = ?1")
    List<BankAccount> findBankAccountsByHolders(int customer);

    BankAccount findBankAccountByPrimaryHolder(Customer customer);

}
