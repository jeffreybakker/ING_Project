package honours.ing.banq.account;

import honours.ing.banq.customer.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Interacts with the <code>BankAccount</code> table of the database.
 * @author Jeffrey Bakker
 */
public interface BankAccountRepository extends JpaRepository<BankAccount, Integer> {

    /**
     * Returns a list of all accounts that have the given holder.
     * @param holder the holder
     * @return a list of all accounts with the given holder
     */
    List<BankAccount> findByHolders(Customer holder);

}
