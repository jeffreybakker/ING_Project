package honours.ing.banq.customer;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author Jeffrey Bakker
 */
public interface CustomerRepository extends JpaRepository<Customer, Integer> {

    /**
     * Returns all customers with the given first name.
     * @param name the first name
     * @return a list of customers with the given first name
     */
    List<Customer> findByName(String name);

    /**
     * Returns all customers with the given surname.
     * @param surname the surname
     * @return a list of customers with the given surname
     */
    List<Customer> findBySurname(String surname);

    /**
     * Returns a list of customers with the given combination of first name and surname.
     * @param name the first name
     * @param surname the surname
     * @return a list of customers that match the given combination of first name and surname
     */
    List<Customer> findByNameAndSurname(String name, String surname);

    /**
     * Returns the customer with the given social security number.
     * @param socialSecurityNumber the social security number
     * @return the customer with the given social security number
     */
    Customer findBySocialSecurityNumber(Integer socialSecurityNumber);

    /**
     * Returns the customer with the given email address.
     * @param email the email address
     * @return the customer with the given email address
     */
    Customer findByEmail(String email);

}
