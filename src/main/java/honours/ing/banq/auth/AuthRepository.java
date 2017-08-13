package honours.ing.banq.auth;

import honours.ing.banq.customer.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * The repository for the {@link Authentication} entity.
 * @author Jeffrey Bakker
 * @since 14-5-17
 */
public interface AuthRepository extends JpaRepository<Authentication, Long> {

    /**
     * Returns the entity with the given token.
     * @param token the token
     * @return the entity with the given token
     */
    Authentication findByToken(String token);

    /**
     * Returns all {@link Authentication} objects for the given customer.
     * @param customer the customer
     * @return a list of {@link Authentication} objects
     */
    List<Authentication> findByCustomer(Customer customer);

}
