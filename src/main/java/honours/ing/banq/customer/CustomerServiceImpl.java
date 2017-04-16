package honours.ing.banq.customer;

import com.googlecode.jsonrpc4j.spring.AutoJsonRpcServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author jeffrey
 * @since 17-4-17
 */
@Service
@AutoJsonRpcServiceImpl
public class CustomerServiceImpl implements CustomerService {

    @Override
    public Customer createCustomer(String initials, String name, String surname, Date birthDate, Integer socialSecurityNumber, String address, String telephoneNumber, String email) {
        Customer res = new Customer(initials, name, surname, birthDate, socialSecurityNumber, address, telephoneNumber, email);
        // TODO: Save
        return res;
    }

}
