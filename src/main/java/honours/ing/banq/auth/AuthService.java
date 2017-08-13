package honours.ing.banq.auth;

import com.googlecode.jsonrpc4j.JsonRpcParam;
import com.googlecode.jsonrpc4j.JsonRpcService;
import honours.ing.banq.error.InvalidParamValueError;
import honours.ing.banq.account.model.Account;
import honours.ing.banq.account.model.BankAccount;
import honours.ing.banq.auth.bean.AuthTokenBean;
import honours.ing.banq.card.Card;
import honours.ing.banq.customer.Customer;
import honours.ing.banq.error.AuthenticationError;
import honours.ing.banq.error.CardBlockedError;
import honours.ing.banq.error.InvalidPINError;
import honours.ing.banq.error.NotAuthorizedError;

/**
 * @author jeffrey
 * @since 14-5-17
 */
@JsonRpcService("/api/auth")
public interface AuthService {

    /**
     * Logs in the user and returns an unique authentication token that the user can use in order to authorize his / her
     * actions in the system.
     * @param username the username of the user that wants to log in
     * @param password the corresponding password
     * @return an authentication token
     * @throws AuthenticationError if the username of password is incorrect
     */
    AuthTokenBean getAuthToken(@JsonRpcParam("username") String username, @JsonRpcParam("password") String password) throws AuthenticationError;

    /**
     * Deletes all authentication tokens for a given customer.
     * @param customer the customer
     */
    void deleteForCustomer(Customer customer);

    /**
     * Returns the authorized card to which the user has access.
     * @param token the authentication token of the user
     * @param iBan the iBan of the user's bank account
     * @param pinCard the card number of the pin card of the user
     * @return the card which the customer is authorized to access
     * @throws NotAuthorizedError if the user is not authorized to access the card
     * @throws InvalidPINError if the card is not valid
     */
    Card getAuthorizedCard(String token, String iBan, String pinCard) throws NotAuthorizedError, InvalidPINError;

    /**
     * Returns the authenticated customer for the given authentication token.
     * @param token the authentication token
     * @return the authenticated customer
     * @throws NotAuthorizedError if there is no user with the given authentication token
     */
    Customer getAuthorizedCustomer(String token) throws NotAuthorizedError;

    /**
     * Returns the authorized bank account for the given token and iBAN.
     * @param token the authentication token
     * @param iBAN  the iBAN for which to find the account
     * @return the authorized account
     * @throws NotAuthorizedError if the user is not authorized to access the given account
     * @throws InvalidParamValueError if the account does not exist
     */
    Account getAuthorizedAccount(String token, String iBAN) throws NotAuthorizedError, InvalidParamValueError;

    /**
     * Returns the authorized bank account for the given credentials.
     * @param iBAN    the iBAN of the account
     * @param pinCard the PIN card's card number
     * @param pinCode the PIN card's PIN code
     * @return the authorized {@link BankAccount}
     * @throws InvalidPINError if the PIN card / PIN code combination is invalid
     * @throws CardBlockedError if the card is blocked
     */
    BankAccount getAuthorizedBankAccount(String iBAN, String pinCard, String pinCode) throws InvalidPINError, CardBlockedError;

    /**
     * Returns the authorized bank account for the given credentials.
     * @param token the authentication token
     * @param iBAN  the iBAN of the account the user wants to access
     * @return the authorized bank account
     * @throws NotAuthorizedError if the user is not authorized to access the account
     * @throws InvalidParamValueError if the bank account is not found
     */
    BankAccount getAuthorizedBankAccount(String token, String iBAN) throws NotAuthorizedError, InvalidParamValueError;

}
