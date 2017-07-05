package honours.ing.banq.auth;

import com.googlecode.jsonrpc4j.JsonRpcParam;
import com.googlecode.jsonrpc4j.JsonRpcService;
import honours.ing.banq.account.BankAccount;
import honours.ing.banq.customer.Customer;

/**
 * @author jeffrey
 * @since 14-5-17
 */
@JsonRpcService("/api/auth")
public interface AuthService {

    AuthToken getAuthToken(@JsonRpcParam("username") String username, @JsonRpcParam("password") String password) throws AuthenticationError;

    Customer getAuthorizedCustomer(String token) throws NotAuthorizedError;
    BankAccount getAuthorizedAccount(String iBAN, String pinCard, String pinCode) throws InvalidPINError;

}
