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

    Transaction transfer(@JsonRpcParam("sourceAccountId") Integer sourceAccountId,
                         @JsonRpcParam("destinationAccountId") Integer destinationAccountId,
                         @JsonRpcParam("amount") Double amount);

    Transaction deposit(@JsonRpcParam("accountId") Integer accountId, @JsonRpcParam("amount") Double amount);
    Transaction withdraw(@JsonRpcParam("accountId") Integer accountId, @JsonRpcParam("amount") Double amount);

    List<Transaction> findBySource(@JsonRpcParam("accountId") Integer accountId);
    List<Transaction> findByDestination(@JsonRpcParam("accountId") Integer accountId);
    List<Transaction> findByAccount(@JsonRpcParam("accountId") Integer accountId);

}
