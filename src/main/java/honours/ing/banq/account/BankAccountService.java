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

    /**
     * Creates a new account with the given parameters.
     * @param holders a list with the holders of the account
     * @param balance the initial balance of the account
     * @return the <code>BankAccount</code> object that was created
     */
    BankAccount createAccount(@JsonRpcParam("holders") List<Integer> holders, @JsonRpcParam("balance") Double balance);

    /**
     * Returns the balance of the bank account with the given id.
     * @param id the id of the bank account of which the balance is requested
     * @return the balance of the account
     */
    Double getBalance(@JsonRpcParam("id") Integer id);

    /**
     * Sets the balance of the bank account with the given id.
     * @param id the id of the bank account of which the balance should be updated
     * @param balance the new balance of the account
     * @return the new balance of the account
     */
    Double setBalance(@JsonRpcParam("id") Integer id, @JsonRpcParam("balance") Double balance);

    /**
     * Deletes a bank account with the given id.
     * @param id the id of the bank account that should be removed
     */
    void deleteAccount(@JsonRpcParam("id") Integer id);

    /**
     * Returns the bank account that has the given id.
     * @param id the id
     * @return the bank account with the given id
     */
    BankAccount findById(@JsonRpcParam("id") Integer id);

    /**
     * Returns a list of bank accounts of which the customer with the given id is a holder.
     * @param id the id of the holder
     * @return a list of bank accounts
     */
    List<BankAccount> findByHolder(@JsonRpcParam("holderId") Integer id);

    /**
     * Adds a customer with the given <code>holderId</code> as a holder to the bank account with the given id.
     * @param id the id of the account
     * @param holderId the id of the new holder
     */
    void addHolder(@JsonRpcParam("id") Integer id, @JsonRpcParam("holderId") Integer holderId);

    /**
     * Returns a list of all holders of a bank account.
     * @param id the id of the bank account
     * @return a list of customers that are holders of the account
     */
    List<Customer> listHolders(@JsonRpcParam("id") Integer id);

    /**
     * Removes a holder with the given <code>holderId</code> from the bank account with the given id.
     * @param id the id of the bank account
     * @param holderId the id of the holder that should be removed from the list of holders
     */
    void removeHolder(@JsonRpcParam("id") Integer id, @JsonRpcParam("holderId") Integer holderId);

}
