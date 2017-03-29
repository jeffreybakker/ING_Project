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

    @Transactional
    public void remove(BankAccount account) {
        repository.delete(account);
    }

    @Transactional
    public BankAccount save(BankAccount account) {
        return repository.save(account);
    }

    public List<BankAccount> findAccountsByHolder(Customer holder) {
        return repository.findByHolders(holder);
    }

    public BankAccount findAccountById(int id) {
        return repository.findOne(id);
    }

    public List<BankAccount> findAll() {
        return repository.findAll();
    }

}
