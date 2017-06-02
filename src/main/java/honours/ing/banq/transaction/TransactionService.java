package honours.ing.banq.transaction;

import com.googlecode.jsonrpc4j.JsonRpcParam;
import com.googlecode.jsonrpc4j.JsonRpcService;
import honours.ing.banq.account.BankAccount;

import java.util.List;

/**
 * @author jeffrey
 * @since 24-4-17
 */
@JsonRpcService("/api")
public interface TransactionService {

    void depositIntoAccount();

}
