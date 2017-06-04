package honours.ing.banq.account;

import honours.ing.banq.customer.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Interacts with the <code>BankAccount</code> table of the database.
 * @author Jeffrey Bakker
 */
public interface BankAccountRepository extends JpaRepository<BankAccount, Integer> {

    List<BankAccount> findBankAccountsByHolders(Customer customer);

}
