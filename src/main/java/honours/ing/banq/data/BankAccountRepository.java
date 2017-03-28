package honours.ing.banq.data;

import honours.ing.banq.model.BankAccount;
import honours.ing.banq.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author Jeffrey Bakker
 */
public interface BankAccountRepository extends JpaRepository<BankAccount, Long> {

    List<BankAccount> findByHolder(Customer holder);

}
