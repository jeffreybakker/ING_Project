package honours.ing.banq.account;

import com.googlecode.jsonrpc4j.spring.AutoJsonRpcServiceImpl;
import honours.ing.banq.InvalidParamValueError;
import honours.ing.banq.account.bean.NewAccountBean;
import honours.ing.banq.auth.AuthRepository;
import honours.ing.banq.auth.AuthService;
import honours.ing.banq.auth.AuthenticationError;
import honours.ing.banq.auth.NotAuthorizedError;
import honours.ing.banq.card.Card;
import honours.ing.banq.card.CardRepository;
import honours.ing.banq.card.CardUtil;
import honours.ing.banq.customer.Customer;
import honours.ing.banq.customer.CustomerRepository;
import honours.ing.banq.time.TimeService;
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
@Transactional
public class BankAccountServiceImpl implements BankAccountService {

    // Services
    @Autowired
    private AuthService auth;

    @Autowired
    private TimeService timeService;

    // Repositories
    @Autowired
    private BankAccountRepository repository;

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private BankAccountRepository bankAccountRepository;

    @Autowired
    private AuthRepository authRepository;

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

        Card card = null;
        try {
            card = new Card(auth.getAuthToken(username, password).getAuthToken(), customer, account, CardUtil
                    .generateCardNumber(cardRepository), timeService.getDate().getDate());
        } catch (AuthenticationError e) {
            // Should be impossible
            e.printStackTrace();
        }
        cardRepository.save(card);

        return new NewAccountBean(card);
    }

    @Override
    public NewAccountBean openAdditionalAccount(String authToken) throws NotAuthorizedError {
        Customer customer = auth.getAuthorizedCustomer(authToken);

        BankAccount account = new BankAccount(customer);
        repository.save(account);

        Card card = new Card(authToken, customer, account, CardUtil.generateCardNumber(cardRepository), timeService.getDate().getDate());
        cardRepository.save(card);

        return new NewAccountBean(card);
    }

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

        // Delete cards
        List<Card> cards = cardRepository.findByAccount(account);
        cardRepository.delete(cards);

        // Delete BankAccount
        repository.delete(account);

        // Delete Customer
        BankAccount primaryAccount = bankAccountRepository.findBankAccountByPrimaryHolder(customer);
        List<BankAccount> heldAccounts = bankAccountRepository.findBankAccountsByHolders(customer.getId());
        if (primaryAccount == null && (heldAccounts == null || heldAccounts.isEmpty())) {
            authRepository.deleteAllByCustomer(customer);
            customerRepository.delete(customer);
        }
    }

}