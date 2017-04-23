package honours.ing.banq.card;

import com.googlecode.jsonrpc4j.spring.AutoJsonRpcServiceImpl;
import honours.ing.banq.account.BankAccount;
import honours.ing.banq.account.BankAccountRepository;
import honours.ing.banq.customer.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author jeffrey
 * @since 24-4-17
 */
@Service
@AutoJsonRpcServiceImpl
@Transactional(readOnly = true)
public class CardServiceImpl implements CardService {

    @Autowired
    private CardRepository repository;

    @Autowired
    private BankAccountRepository accountRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Transactional
    @Override
    public Card createCard(Integer holderId, Integer accountId) {
        Card res = new Card(customerRepository.findOne(holderId), accountRepository.findOne(accountId));
        repository.save(res);
        return res;
    }

    @Transactional
    @Override
    public void deleteCard(Integer id) {
        repository.delete(id);
    }

    @Override
    public Card findByHolderAndAccount(Integer holderId, Integer accountId) {
        return repository.findByHolderAndAccount(
                customerRepository.findOne(holderId),
                accountRepository.findOne(accountId)
        );
    }

    @Override
    public List<Card> findByHolder(Integer holderId) {
        return repository.findByHolder(customerRepository.findOne(holderId));
    }

    @Override
    public List<Card> findByAccount(Integer accountId) {
        return repository.findByAccount(accountRepository.findOne(accountId));
    }
}
