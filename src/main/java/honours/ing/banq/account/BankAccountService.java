package honours.ing.banq.account;

import com.googlecode.jsonrpc4j.JsonRpcParam;
import com.googlecode.jsonrpc4j.JsonRpcService;
import honours.ing.banq.customer.Customer;

import java.util.List;

/**
 * @author jeffrey
 * @since 17-4-17
 */
@JsonRpcService("/api/account")
public interface BankAccountService {

    BankAccount createAccount(@JsonRpcParam("holders") List<Integer> holders, @JsonRpcParam("balance") Double balance);

    Double getBalance(@JsonRpcParam("id") Integer id);
    Double setBalance(@JsonRpcParam("id") Integer id, @JsonRpcParam("balance") Double balance);

    void deleteAccount(@JsonRpcParam("id") Integer id);

    BankAccount findById(@JsonRpcParam("id") Integer id);
    List<BankAccount> findByHolder(@JsonRpcParam("holderId") Integer id);

    void addHolder(@JsonRpcParam("id") Integer id, @JsonRpcParam("holderId") Integer holderId);
    List<Customer> listHolders(@JsonRpcParam("id") Integer id);
    void removeHolder(@JsonRpcParam("id") Integer id, @JsonRpcParam("holderId") Integer holderId);

}
