package honours.ing.banq.account;

import com.googlecode.jsonrpc4j.spring.AutoJsonRpcServiceImpl;
import honours.ing.banq.InvalidParamValueError;
import honours.ing.banq.account.bean.NewAccountBean;
import honours.ing.banq.auth.AuthService;
import honours.ing.banq.auth.NotAuthorizedError;
import honours.ing.banq.card.Card;
import honours.ing.banq.card.CardRepository;
import honours.ing.banq.card.CardUtil;
import honours.ing.banq.customer.Customer;
import honours.ing.banq.customer.CustomerRepository;
import honours.ing.banq.util.IBANUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author jeffrey
 * @since 17-4-17
 */
@Service
@AutoJsonRpcServiceImpl
@Transactional(readOnly = true)
public class BankAccountServiceImpl implements BankAccountService {

    // Services
    @Autowired
    private AuthService auth;

    // Repositories
    @Autowired
    private BankAccountRepository repository;

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Transactional
    @Override
    public NewAccountBean openAccount(String name, String surname, String initials, String dob, String ssn, String
            address, String telephoneNumber, String email, String username, String password) throws
            InvalidParamValueError {
        Customer customer = new Customer(name, surname, initials, dob, ssn, address, telephoneNumber, email,
                                         username, password);
        try {
            customerRepository.save(customer);
        } catch (Exception e) {
            throw new InvalidParamValueError("Could not create customer because the given information was invalid");
        }

        BankAccount account = new BankAccount(customer);
        repository.save(account);

        Card card = new Card(customer, account, CardUtil.generateCardNumber(cardRepository));
        cardRepository.save(card);

        return new NewAccountBean(card);
    }

    @Transactional
    @Override
    public NewAccountBean openAdditionalAccount(String authToken) throws NotAuthorizedError {
        Customer customer = auth.getAuthorizedCustomer(authToken);

        BankAccount account = new BankAccount(customer);
        repository.save(account);

        Card card = new Card(customer, account, CardUtil.generateCardNumber(cardRepository));
        cardRepository.save(card);

        return new NewAccountBean(card);
    }

    @Transactional
    @Override
    public void closeAccount(String authToken, String iBAN) throws NotAuthorizedError, InvalidParamValueError {
        Customer customer = auth.getAuthorizedCustomer(authToken);

        long accountNumber = IBANUtil.getAccountNumber(iBAN);
        BankAccount account = repository.findOne((int) accountNumber);

        if (account == null) {
            throw new InvalidParamValueError("Bank account does not exist");
        }

        if (!account.getPrimaryHolder().equals(customer)) {
            throw new NotAuthorizedError();
        }

        List<Card> cards = cardRepository.findByAccount(account);
        cardRepository.delete(cards);

        repository.delete(account);
    }
}