package honours.ing.banq.customer;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author Jeffrey Bakker
 */
public interface CustomerRepository extends JpaRepository<Customer, Integer> {

    Customer findByUsername(String username);
    Customer findByUsernameAndPassword(String username, String password);

    Customer findBySsn(String ssn);

}
