package honours.ing.banq.transaction;

import com.googlecode.jsonrpc4j.JsonRpcParam;
import com.googlecode.jsonrpc4j.JsonRpcService;
import honours.ing.banq.error.InvalidParamValueError;
import honours.ing.banq.account.model.Account;
import honours.ing.banq.account.model.BankAccount;
import honours.ing.banq.error.CardBlockedError;
import honours.ing.banq.error.InvalidPINError;
import honours.ing.banq.error.NotAuthorizedError;
import honours.ing.banq.card.Card;
import honours.ing.banq.customer.Customer;

import java.math.BigDecimal;

/**
 * @author Kevin Witlox
 * @since 4-6-2017
 */
@JsonRpcService("/api/transaction")
public interface TransactionService {

    /**
     * Deposit money into a bank account.
     *
     * @param iBAN    The number of the {@link BankAccount} to deposit the money into
     * @param pinCard The {@link Card} used to perform the transaction
     * @param pinCode The code used for authorizing the use of the {@link Card}
     * @param amount  The amount to be deposited
     */
    void depositIntoAccount(
            @JsonRpcParam("iBAN") String iBAN, @JsonRpcParam("pinCard") String pinCard,
            @JsonRpcParam("pinCode") String pinCode, @JsonRpcParam("amount") Double amount)
            throws InvalidParamValueError, InvalidPINError, CardBlockedError;

    /**
     * Transfer money to a {@link BankAccount} using a {@link Card}
     *
     * @param sourceIBAN The {@link BankAccount} belonging to the {@link Card}
     * @param targetIBAN The {@link BankAccount} to transfer the money to
     * @param pinCard    The {@link Card} used to perform the transaction
     * @param pinCode    The code used for authorizing the use of the {@link Card}
     * @param amount     The amount to be transferred
     * @throws InvalidParamValueError
     * @throws InvalidPINError
     */
    void payFromAccount(
            @JsonRpcParam("sourceIBAN") String sourceIBAN, @JsonRpcParam("targetIBAN") String targetIBAN,
            @JsonRpcParam("pinCard") String pinCard, @JsonRpcParam("pinCode") String pinCode,
            @JsonRpcParam("amount") Double amount) throws InvalidParamValueError, InvalidPINError, CardBlockedError;

    void forceTransactionAccount(Account account, BigDecimal amount, String description);

    /**
     * Transer money from one {@link BankAccount} to another {@link BankAccount}.
     *
     * @param authToken   The authorization token of the {@link Customer} to transfer the money from
     * @param sourceIBAN  The {@link BankAccount} to transfer the money from ({@link Customer} must have access to it)
     * @param targetIBAN  The {@link BankAccount} to transfer the money to
     * @param targetName  The name of the {@link Customer} holding the receiving {@link BankAccount}
     * @param amount      The amount to be transferred
     * @param description A description for the transaction
     * @throws InvalidParamValueError
     * @throws NotAuthorizedError
     */
    void transferMoney(
            @JsonRpcParam("authToken") String authToken, @JsonRpcParam("sourceIBAN") String sourceIBAN,
            @JsonRpcParam("targetIBAN") String targetIBAN, @JsonRpcParam ("targetName") String targetName,
            @JsonRpcParam("amount") Double amount, @JsonRpcParam("description") String description)
            throws InvalidParamValueError, NotAuthorizedError;

}
