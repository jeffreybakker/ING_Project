package honours.ing.banq.repository;

import honours.ing.banq.model.Customer;
import org.springframework.data.repository.CrudRepository;

/**
 * @author Jeffrey Bakker
 */
public interface CustomerRepository extends CrudRepository<Customer, Long> {
}
