package honours.ing.banq.transaction;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * The repository for the {@link Transaction} entity.
 * @author Jeffrey Bakker
 * @since 24-4-17
 */
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {

    /**
     * Lists all of the transactions with the given source or destination iBAN.
     * @param src the source iBAN
     * @param dst the destination iBAN
     * @return a list of {@link Transaction}s
     */
    List<Transaction> findBySourceOrDestinationOrderByDateDesc(String src, String dst);

}
