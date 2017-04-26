package honours.ing.banq.transaction;

import honours.ing.banq.account.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author jeffrey
 * @since 24-4-17
 */
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {

    /**
     * Returns a list of all transactions of the given source account.
     * @param source the source account
     * @return a list of transactions
     */
    List<Transaction> findBySource(BankAccount source);

    /**
     * Returns a list of all transactions of the given destination account.
     * @param destination the destination account
     * @return a list of transactions
     */
    List<Transaction> findByDestination(BankAccount destination);

}
