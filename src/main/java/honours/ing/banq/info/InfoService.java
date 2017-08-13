package honours.ing.banq.info;

import com.googlecode.jsonrpc4j.JsonRpcParam;
import com.googlecode.jsonrpc4j.JsonRpcService;
import honours.ing.banq.error.InvalidParamValueError;
import honours.ing.banq.account.BankAccount;
import honours.ing.banq.error.NotAuthorizedError;
import honours.ing.banq.customer.Customer;
import honours.ing.banq.info.bean.BalanceBean;
import honours.ing.banq.info.bean.BankAccountAccessBean;
import honours.ing.banq.info.bean.UserAccessBean;
import honours.ing.banq.transaction.Transaction;

import java.util.List;

/**
 * @author Kevin Witlox
 */
@JsonRpcService("/api/info")
public interface InfoService {

    /**
     * Returns the balance of a {@link BankAccount}.
     *
     * @param autToken The authorization token of a {@link Customer} holding the {@link
     *                 BankAccount}
     * @param iBAN     The iBAN identifying the {@link BankAccount}
     * @return The balance of the {@link BankAccount}
     * @throws InvalidParamValueError
     * @throws NotAuthorizedError     Thrown when something went wrong with the authentication
     *                                module
     */
    BalanceBean getBalance(@JsonRpcParam("authToken") String autToken, @JsonRpcParam("iBAN")
            String iBAN) throws InvalidParamValueError, NotAuthorizedError;

    /**
     * Returns a {@link List} of {@link Transaction} entities for a specific {@link BankAccount}.
     *
     * @param authToken        The authorization token identifying the {@link Customer}
     * @param iBAN             The iBAN of the {@link BankAccount} held by the {@link Customer}
     * @param nrOfTransactions The number of transactions that should be returned.
     *                         'nrOfTransactions' = 1 will return the most recent transaction.
     * @return A {@link List} of {@link Transaction} entities
     * @throws InvalidParamValueError
     * @throws NotAuthorizedError     Thrown when something went wrong with the authentication
     *                                module
     */
    List<Transaction> getTransactionsOverview(@JsonRpcParam("authToken") String authToken,
                                              @JsonRpcParam("iBAN") String iBAN, @JsonRpcParam
                                                      ("nrOfTransactions") Integer
                                                      nrOfTransactions) throws
            InvalidParamValueError, NotAuthorizedError;

    /**
     * Returns all {@link BankAccount} entities that can be accessed by the currently authorized
     * {@link Customer}.
     *
     * @param authToken The authorization token to authorize and identify the {@link Customer}
     * @return A {@link List} of {@link UserAccessBean}
     * @throws InvalidParamValueError
     * @throws NotAuthorizedError     Thrown when something went wrong with the authentication
     *                                module
     */
    List<UserAccessBean> getUserAccess(@JsonRpcParam("authToken") String authToken) throws
            InvalidParamValueError, NotAuthorizedError;

    /**
     * Returns all {@link Customer} entities that have access to a certain {@link BankAccount}.
     *
     * @param authToken The authorization token of the {@link Customer} owning the {@link
     *                  BankAccount}
     * @return A {@link List} of {@link BankAccountAccessBean}
     * @throws InvalidParamValueError
     * @throws NotAuthorizedError     Thrown when something went wrong with the authentication
     *                                module
     */
    List<BankAccountAccessBean> getBankAccountAccess(@JsonRpcParam("authToken") String authToken,
                                                     @JsonRpcParam("iBAN") String iBAN) throws
            InvalidParamValueError, NotAuthorizedError;

}
