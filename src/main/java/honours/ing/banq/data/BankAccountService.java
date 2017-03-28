package honours.ing.banq.data;

import honours.ing.banq.model.BankAccount;
import honours.ing.banq.model.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Jeffrey Bakker
 */
@Repository
@Transactional(readOnly = true)
public class BankAccountService {

    @Autowired
    private BankAccountRepository repository;

    public List<BankAccount> findByCustomer(Customer customer) {
        return repository.findByHolder(customer);
    }

    @Transactional
    public BankAccount save(BankAccount account) {
        return repository.save(account);
    }

}
