package honours.ing.banq.transaction;

import com.googlecode.jsonrpc4j.spring.AutoJsonRpcServiceImpl;
import honours.ing.banq.account.BankAccount;
import honours.ing.banq.account.BankAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;

/**
 * @author jeffrey
 * @since 24-4-17
 */
@Service
@AutoJsonRpcServiceImpl
@Transactional(readOnly = true)
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionRepository repository;

    @Autowired
    private BankAccountRepository accountRepository;

    @Transactional
    @Override
    public Transaction transfer(Integer sourceAccountId, Integer destinationAccountId, Double amount) {
        BankAccount source = accountRepository.findOne(sourceAccountId);
        BankAccount destination = accountRepository.findOne(destinationAccountId);

        source.setBalance(source.getBalance() - amount);
        destination.setBalance(destination.getBalance() + amount);

        Transaction res = new Transaction(
                source, source.getBalance(),
                destination, destination.getBalance(),
                Calendar.getInstance().getTime());
        repository.save(res);
        return res;
    }

    @Transactional
    @Override
    public Transaction deposit(Integer accountId, Double amount) {
        BankAccount destination = accountRepository.findOne(accountId);

        destination.setBalance(destination.getBalance() + amount);

        Transaction res = new Transaction(
                null, 0.0,
                destination, destination.getBalance(),
                Calendar.getInstance().getTime());
        repository.save(res);
        return res;
    }

    @Transactional
    @Override
    public Transaction withdraw(Integer accountId, Double amount) {
        BankAccount source = accountRepository.findOne(accountId);

        source.setBalance(source.getBalance() - amount);

        Transaction res = new Transaction(
                source, source.getBalance(),
                null, 0.0,
                Calendar.getInstance().getTime());
        repository.save(res);
        return res;
    }

    @Override
    public List<Transaction> findBySource(Integer accountId) {
        return repository.findBySource(accountRepository.findOne(accountId));
    }

    @Override
    public List<Transaction> findByDestination(Integer accountId) {
        return repository.findByDestination(accountRepository.findOne(accountId));
    }

    @Override
    public List<Transaction> findByAccount(Integer accountId) {
        BankAccount account = accountRepository.findOne(accountId);
        List<Transaction> res = new ArrayList<>();

        res.addAll(repository.findBySource(account));
        res.addAll(repository.findByDestination(account));
        res.sort(Comparator.comparing(Transaction::getDate));
        return res;
    }

}
