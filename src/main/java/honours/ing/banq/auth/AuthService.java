package honours.ing.banq.auth;

import com.googlecode.jsonrpc4j.JsonRpcParam;
import com.googlecode.jsonrpc4j.JsonRpcService;
import honours.ing.banq.InvalidParamValueError;
import honours.ing.banq.account.Account;
import honours.ing.banq.account.BankAccount;
import honours.ing.banq.auth.bean.AuthToken;
import honours.ing.banq.card.Card;
import honours.ing.banq.customer.Customer;

/**
 * @author jeffrey
 * @since 14-5-17
 */
@JsonRpcService("/api/auth")
public interface AuthService {

    AuthToken getAuthToken(@JsonRpcParam("username") String username, @JsonRpcParam("password") String password) throws AuthenticationError;

    void deleteForCustomer(Customer customer);

    Card getAuthorizedCard(String token, String iBan, String pinCard) throws NotAuthorizedError, InvalidPINError;
    Customer getAuthorizedCustomer(String token) throws NotAuthorizedError;

    Account getAuthorizedAccount(String token, String iBAN) throws NotAuthorizedError, InvalidParamValueError;

    BankAccount getAuthorizedBankAccount(String iBAN, String pinCard, String pinCode) throws InvalidPINError, CardBlockedError;
    BankAccount getAuthorizedBankAccount(String token, String iBAN) throws NotAuthorizedError, InvalidParamValueError;

}
