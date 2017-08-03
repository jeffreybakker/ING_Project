package honours.ing.banq.auth;

import honours.ing.banq.customer.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author jeffrey
 * @since 14-5-17
 */
public interface AuthRepository extends JpaRepository<Authentication, Long> {

    Authentication findByToken(String token);

    List<Authentication> findByCustomer(Customer customer);

}
