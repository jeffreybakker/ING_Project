package honours.ing.banq.auth;

import com.googlecode.jsonrpc4j.spring.AutoJsonRpcServiceImpl;
import honours.ing.banq.customer.Customer;
import honours.ing.banq.customer.CustomerRepository;
import org.aspectj.weaver.ast.Not;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Random;

/**
 * @author jeffrey
 * @since 14-5-17
 */
@Service
@AutoJsonRpcServiceImpl
@Transactional(readOnly = true)
public class AuthServiceImpl implements AuthService {

    private static final int TOKEN_LENGTH = 255;
    private static final String CHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789-_=+[{]}|;:/?.>,<!@#$%&*()";

    private static final int VALIDITY = 60 * 60 * 24; // one day

    @Autowired
    private AuthRepository repository;

    @Autowired
    private CustomerRepository customerRepository;

    private Random random = new Random();

    @Transactional
    @Override
    public String getAuthToken(String username, String password) throws AuthenticationError {
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
            } catch (Exception ignored) { }
        }

        return auth.getToken();
    }

    @Override
    public Customer getAuthorizedCustomer(String token) throws NotAuthorizedError {
        Authentication auth = repository.findByToken(token);
        if (auth.hasExpired()) {
            throw new NotAuthorizedError();
        }

        return auth.getCustomer();
    }

    private String genToken() {
        char[] res = new char[TOKEN_LENGTH];

        for (int i = 0; i < TOKEN_LENGTH; i++) {
            res[i] = CHARS.charAt(random.nextInt(CHARS.length()));
        }

        return new String(res);
    }

}
