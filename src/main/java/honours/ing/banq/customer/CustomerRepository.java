package honours.ing.banq.customer;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author Jeffrey Bakker
 */
public interface CustomerRepository extends JpaRepository<Customer, Integer> {

    Customer findByUsernameAndPassword(String username, String password);

}
