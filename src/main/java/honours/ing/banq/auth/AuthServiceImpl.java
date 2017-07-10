package honours.ing.banq.auth;

import com.googlecode.jsonrpc4j.spring.AutoJsonRpcServiceImpl;
import honours.ing.banq.InvalidParamValueError;
import honours.ing.banq.account.BankAccount;
import honours.ing.banq.account.BankAccountRepository;
import honours.ing.banq.card.Card;
import honours.ing.banq.card.CardRepository;
import honours.ing.banq.customer.Customer;
import honours.ing.banq.customer.CustomerRepository;
import honours.ing.banq.util.IBANUtil;
import org.aspectj.weaver.ast.Not;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Objects;
import java.util.Random;

/**
 * @author jeffrey
 * @since 14-5-17
 */
@Service
@AutoJsonRpcServiceImpl
@Transactional
public class AuthServiceImpl implements AuthService {

    private static final int TOKEN_LENGTH = 255;
    private static final String CHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789-_=+[{]}|;:/?.>,<!@#$%&*()";

    private static final int VALIDITY = 60 * 60 * 24; // one day

    @Autowired
    private AuthRepository repository;

    @Autowired
    private BankAccountRepository accountRepository;

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private CustomerRepository customerRepository;

    private Random random = new Random();

    @Transactional
    @Override
    public AuthToken getAuthToken(String username, String password) throws AuthenticationError {
        Customer customer = customerRepository.findByUsernameAndPassword(username, password);
        if (customer == null) {
            throw new AuthenticationError();
        }

        Authentication auth = null;
        Calendar exp = Calendar.getInstance();
        exp.add(Calendar.SECOND, VALIDITY);

        while (auth == null) {
            try {
                auth = new Authentication(customer, genToken(), exp.getTime());
                repository.save(auth);
            } catch (Exception e) {
                auth = null;
            }
        }

        return new AuthToken(auth.getToken());
    }

    @Override
    public Customer getAuthorizedCustomer(String token) throws NotAuthorizedError {
        if (token == null || token.length() == 0) {
            throw new InvalidParamValueError("Token cannot be null/empty");
        }

        Authentication auth = repository.findByToken(token);
        if (auth == null || auth.hasExpired()) {
            throw new NotAuthorizedError();
        }

        return auth.getCustomer();
    }

    @Override
    public BankAccount getAuthorizedAccount(String iBAN, String pinCard, String pinCode) throws InvalidPINError {
        if (iBAN == null || iBAN.length() <= 8 || pinCard == null || pinCode == null) {
            throw new InvalidParamValueError("One of the parameters is null or the IBAN is not long enough");
        }
        BankAccount account = accountRepository.findOne((int) IBANUtil.getAccountNumber(iBAN));
        if (account == null) {
            throw new InvalidParamValueError("Account with the given IBAN does not exist in this system");
        }

        Card card = cardRepository.findByAccountAndCardNumber(account, pinCard);
        if (card == null) {
            throw new InvalidParamValueError("Card does not exist");
        }

        if (!card.getPin().equals(pinCode)) {
            throw new InvalidPINError();
        }

        return account;
    }

    private String genToken() {
        char[] res = new char[TOKEN_LENGTH];

        for (int i = 0; i < TOKEN_LENGTH; i++) {
            res[i] = CHARS.charAt(random.nextInt(CHARS.length()));
        }

        return new String(res);
    }

}
