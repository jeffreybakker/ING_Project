package honours.ing.banq.auth;

import honours.ing.banq.customer.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author jeffrey
 * @since 14-5-17
 */
public interface AuthRepository extends JpaRepository<Authentication, Long> {

    Authentication findByToken(String token);
    void deleteAllByCustomer(Customer customer);

}
