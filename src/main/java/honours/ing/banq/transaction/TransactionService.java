package honours.ing.banq.transaction;

import com.googlecode.jsonrpc4j.JsonRpcParam;
import com.googlecode.jsonrpc4j.JsonRpcService;
import honours.ing.banq.account.BankAccount;

import java.util.List;

/**
 * @author jeffrey
 * @since 24-4-17
 */
@JsonRpcService("/api/transaction")
public interface TransactionService {

    /**
     * Transfers money from the source account to the destination account.
     * @param sourceAccountId the id of the source account
     * @param destinationAccountId the id of the destination account
     * @param amount the amount of money to be transferred
     * @return the transaction object
     */
    Transaction transfer(@JsonRpcParam("sourceAccountId") Integer sourceAccountId,
                         @JsonRpcParam("destinationAccountId") Integer destinationAccountId,
                         @JsonRpcParam("amount") Double amount);

    /**
     * Deposits an amount of money onto a given account.
     * @param accountId the id of the account to deposit the money on
     * @param amount the amount of money
     * @return the transaction object
     */
    Transaction deposit(@JsonRpcParam("accountId") Integer accountId, @JsonRpcParam("amount") Double amount);

    /**
     * Withdraws an amount of money from a given account.
     * @param accountId the id of the account to withdraw the money from
     * @param amount the amount of money
     * @return the transaction object
     */
    Transaction withdraw(@JsonRpcParam("accountId") Integer accountId, @JsonRpcParam("amount") Double amount);

    /**
     * Returns a list of transactions with the given source account.
     * @param accountId the id of the source account
     * @return a list of transactions
     */
    List<Transaction> findBySource(@JsonRpcParam("accountId") Integer accountId);

    /**
     * Returns a list of transactions with the given destination account.
     * @param accountId the id of the destination account
     * @return a list of transactions
     */
    List<Transaction> findByDestination(@JsonRpcParam("accountId") Integer accountId);

    /**
     * Returns a list of transactions in which the given account is either the source or the destination.
     * @param accountId the id of the account
     * @return a list of transactions
     */
    List<Transaction> findByAccount(@JsonRpcParam("accountId") Integer accountId);

}
