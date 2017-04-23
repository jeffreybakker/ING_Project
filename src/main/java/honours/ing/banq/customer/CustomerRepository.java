package honours.ing.banq.customer;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author Jeffrey Bakker
 */
public interface CustomerRepository extends JpaRepository<Customer, Integer> {

    List<Customer> findByName(String name);
    List<Customer> findBySurname(String surname);
    List<Customer> findByNameAndSurname(String name, String surname);

    Customer findBySocialSecurityNumber(Integer socialSecurityNumber);
    Customer findByEmail(String email);

}
