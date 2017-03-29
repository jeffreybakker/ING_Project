package honours.ing.banq.data;

import honours.ing.banq.model.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author jeffrey
 * @since 29-3-17
 */
@Repository
@Transactional(readOnly = true)
public class CustomerService {

    @Autowired
    private CustomerRepository repository;

    @Transactional
    public void remove(Customer customer) {
        repository.delete(customer);
    }

    @Transactional
    public Customer save(Customer customer) {
        return repository.save(customer);
    }

    public List<Customer> findAll() {
        return repository.findAll();
    }

    public Customer findCustomerById(int id) {
        return repository.findOne(id);
    }

    public List<Customer> findCustomersByFirstName(String name) {
        return repository.findByName(name);
    }

    public List<Customer> findCustomersBySurName(String name) {
        return repository.findBySurname(name);
    }

    public List<Customer> findCustomersByName(String firstName, String surname) {
        return repository.findByNameAndSurname(firstName, surname);
    }

    public Customer findCustomerBySocialSecurityNumber(int socialSecurityNumber) {
        return repository.findBySocialSecurityNumber(socialSecurityNumber);
    }

    public Customer findCustomerByEmail(String email) {
        return repository.findByEmail(email);
    }

}
