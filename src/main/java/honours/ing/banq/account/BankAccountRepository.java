package honours.ing.banq.account;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Interacts with the <code>BankAccount</code> table of the database.
 * @author Jeffrey Bakker
 */
public interface BankAccountRepository extends JpaRepository<BankAccount, Integer> {



}
