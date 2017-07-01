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
import honours.ing.banq.response.SuperBean;
import honours.ing.banq.response.error.ErrorBean;
import honours.ing.banq.response.error.ErrorCode;
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

    // Repositories
    @Autowired
    private BankAccountRepository repository;

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Transactional
    @Override
    public SuperBean<NewAccountBean> openAccount(String name, String surname, String initials, String dob, String ssn, String address, String telephoneNumber, String email, String username, String password) throws InvalidParamValueError {
        ErrorBean errorBean = null;
        
        if (!Customer.checkDate(dob)) {
            errorBean = new ErrorBean(ErrorCode.INVALIDPARAMVALUE, "The given date is not valid.");
        }

        Customer customer = new Customer(name, surname, initials, dob, ssn, address, telephoneNumber, email, username, password);
        try {
            customerRepository.save(customer);
        } catch (Exception e) {
            errorBean = new ErrorBean(ErrorCode.INVALIDPARAMVALUE, "Customer already exists.");
        }

        BankAccount account = new BankAccount(customer);
        repository.save(account);

        Card card = new Card(customer, account, CardUtil.generateCardNumber(cardRepository));
        cardRepository.save(card);

        return new SuperBean<>(new NewAccountBean(card), errorBean, card.getId().toString());
    }

    @Transactional
    @Override
    public SuperBean<NewAccountBean> openAdditionalAccount(String authToken) throws NotAuthorizedError {
        Customer customer = auth.getAuthorizedCustomer(authToken);

        BankAccount account = new BankAccount(customer);
        repository.save(account);

        Card card = new Card(customer, account, CardUtil.generateCardNumber(cardRepository));
        cardRepository.save(card);

        return new SuperBean<>(new NewAccountBean(card), null, card.getId().toString());
    }

    @Transactional
    @Override
    public SuperBean closeAccount(String authToken, String iBAN) throws NotAuthorizedError, InvalidParamValueError {
        ErrorBean errorBean = null;
        Customer customer = auth.getAuthorizedCustomer(authToken);

        long accountNumber = IBANUtil.getAccountNumber(iBAN);
        BankAccount account = repository.findOne((int) accountNumber);

        if (!account.getPrimaryHolder().equals(customer)) {
            errorBean = new ErrorBean(ErrorCode.AUTHENTICATION, "");
        }

        List<Card> cards = cardRepository.findByAccount(account);
        cardRepository.delete(cards);

        repository.delete(account);

        return new SuperBean<>(null, errorBean, null);
    }

}
