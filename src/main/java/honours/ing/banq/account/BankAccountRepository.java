package honours.ing.banq.account;

import honours.ing.banq.account.BankAccount;
import honours.ing.banq.customer.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author Jeffrey Bakker
 */
public interface BankAccountRepository extends JpaRepository<BankAccount, Integer> {

    List<BankAccount> findByHolders(Customer holder);

}
