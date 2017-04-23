package honours.ing.banq.account;

import com.googlecode.jsonrpc4j.spring.AutoJsonRpcServiceImpl;
import honours.ing.banq.customer.Customer;
import honours.ing.banq.customer.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jeffrey
 * @since 17-4-17
 */
@Service
@AutoJsonRpcServiceImpl
@Transactional(readOnly = true)
public class BankAccountServiceImpl implements BankAccountService {

    @Autowired
    private BankAccountRepository repository;

    @Autowired
    private CustomerRepository customerRepository;

    @Transactional
    @Override
    public BankAccount createAccount(List<Integer> holders, Double balance) {
        List<Customer> customers = new ArrayList<>();
        for (int id : holders) {
            customers.add(customerRepository.findOne(id));
        }

        BankAccount res = new BankAccount(customers, balance);
        repository.save(res);
        return res;
    }

    @Override
    public Double getBalance(Integer id) {
        return repository.findOne(id).getBalance();
    }

    @Transactional
    @Override
    public Double setBalance(Integer id, Double balance) {
        BankAccount res = repository.findOne(id);
        res.setBalance(balance);
        repository.save(res);
        return balance;
    }

    @Transactional
    @Override
    public void deleteAccount(Integer id) {
        repository.delete(id);
    }

    @Override
    public BankAccount findById(Integer id) {
        return repository.findOne(id);
    }

    @Override
    public List<BankAccount> findByHolder(Integer id) {
        return repository.findByHolders(customerRepository.findOne(id));
    }

    @Transactional
    @Override
    public void addHolder(Integer id, Integer holderId) {
        BankAccount res = repository.findOne(id);
        Customer customer = customerRepository.findOne(holderId);

        res.addHolder(customer);
        repository.save(res);
    }

    @Override
    public List<Customer> listHolders(Integer id) {
        return repository.findOne(id).getHolders();
    }

    @Transactional
    @Override
    public void removeHolder(Integer id, Integer holderId) {
        BankAccount res = repository.findOne(id);
        Customer customer = customerRepository.findOne(holderId);

        res.removeHolder(customer);
        repository.save(res);
    }
}
