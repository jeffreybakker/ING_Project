package honours.ing.banq.customer;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * The repository for the {@link Customer} entity.
 * @author Jeffrey Bakker
 */
public interface CustomerRepository extends JpaRepository<Customer, Integer> {

    /**
     * Returns the customer with the given username.
     * @param username the username
     * @return a {@link Customer} object
     */
    Customer findByUsername(String username);

    /**
     * Returns the customer with the given username and password.
     * @param username the username
     * @param password the password
     * @return the customer
     */
    Customer findByUsernameAndPassword(String username, String password);

    /**
     * Returns the customer with the given social security number.
     * @param ssn the social security number
     * @return the customer
     */
    Customer findBySsn(String ssn);

}
