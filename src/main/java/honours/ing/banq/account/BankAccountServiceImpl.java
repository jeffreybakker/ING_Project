package honours.ing.banq.account;

import com.googlecode.jsonrpc4j.spring.AutoJsonRpcServiceImpl;
import honours.ing.banq.InvalidParamValueError;
import honours.ing.banq.account.bean.NewAccountBean;
import honours.ing.banq.account.bean.OverdraftBean;
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

import java.math.BigDecimal;
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
    public NewAccountBean openAccount(String name, String surname, String initials,
                                      String dob, String ssn,
                                      String address, String telephoneNumber,
                                      String email, String username, String password) throws InvalidParamValueError {
        Customer existing = customerRepository.findBySsn(ssn);
        if (existing != null) {
            throw new InvalidParamValueError(
                    "Could not create customer because the given information was invalid");
        }

        Customer customer = new Customer(
                name, surname, initials,
                dob, ssn,
                address, telephoneNumber,
                email, username, password);

        try {
            customerRepository.save(customer);
        } catch (Exception e) {
            throw new InvalidParamValueError(
                    "Could not create customer because the given information was invalid");
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
    public Object closeAccount(String authToken, String iBAN) throws NotAuthorizedError, InvalidParamValueError {
        BankAccount account = auth.getAuthorizedBankAccount(authToken, iBAN);
        Customer customer = auth.getAuthorizedCustomer(authToken);

        if (!account.getPrimaryHolder().equals(customer)) {
            throw new NotAuthorizedError();
        }

        List<Card> cards = cardRepository.findByAccount(account);
        cardRepository.delete(cards);

        repository.delete(account);

        // Delete the customer if that is needed
        List<BankAccount> primaryAccounts = repository.findBankAccountsByPrimaryHolder(customer.getId());
        List<BankAccount> holderAccounts = repository.findBankAccountsByHolders(customer.getId());

        if (primaryAccounts.size() == 0 && holderAccounts.size() == 0) {
            auth.deleteForCustomer(customer);
            customerRepository.delete(customer);
        }

        return new Object();
    }

    @Override
    public OverdraftBean getOverdraftLimit(String authToken, String iBAN) throws NotAuthorizedError, InvalidParamValueError {
        return new OverdraftBean(
                auth.getAuthorizedBankAccount(authToken, iBAN).getCheckingAccount().getOverdraftLimit());
    }

    @Transactional
    @Override
    public Object setOverdraftLimit(String authToken, String iBAN, double overdraftLimit) throws NotAuthorizedError, InvalidParamValueError {
        Customer customer = auth.getAuthorizedCustomer(authToken);
        BankAccount account = auth.getAuthorizedBankAccount(authToken, iBAN);

        if (!account.getPrimaryHolder().equals(customer)) {
            throw new NotAuthorizedError();
        }

        if (overdraftLimit < 0.0d) {
            throw new InvalidParamValueError("The overdraft limit must be a positive value");
        }

        account.getCheckingAccount().setOverdraftLimit(new BigDecimal(overdraftLimit));
        repository.save(account);

        return new Object();
    }

    @Transactional
    @Override
    public Object openSavingsAccount(String authToken, String iBAN) throws InvalidParamValueError, NotAuthorizedError {
        Customer customer = auth.getAuthorizedCustomer(authToken);
        BankAccount account = auth.getAuthorizedBankAccount(authToken, iBAN);

        if (!account.getPrimaryHolder().equals(customer)) {
            throw new NotAuthorizedError();
        }

        account.addSavingAccount();
        repository.save(account);

        return new Object();
    }

    @Override
    public Object closeSavingsAccount(String authToken, String iBAN) throws InvalidParamValueError, NotAuthorizedError {
        Customer customer = auth.getAuthorizedCustomer(authToken);
        BankAccount account = auth.getAuthorizedBankAccount(authToken, iBAN);

        if (!account.getPrimaryHolder().equals(customer)) {
            throw new NotAuthorizedError();
        }

        if (!account.getSavingAccount().getBalance().equals(BigDecimal.ZERO)) {
            throw new InvalidParamValueError("Account balance needs to be cleared");
        }

        account.removeSavingAccount();
        repository.save(account);

        return new Object();
    }
}