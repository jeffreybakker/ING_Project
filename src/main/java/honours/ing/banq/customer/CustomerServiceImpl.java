package honours.ing.banq.customer;

import com.googlecode.jsonrpc4j.spring.AutoJsonRpcServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author jeffrey
 * @since 17-4-17
 */
@Service
@AutoJsonRpcServiceImpl
@Transactional(readOnly = true)
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository repository;

    @Transactional
    @Override
    public Customer createCustomer(String initials, String name, String surname, Date birthDate, Integer socialSecurityNumber, String address, String telephoneNumber, String email) {
        Customer res = new Customer(initials, name, surname, birthDate, socialSecurityNumber, address, telephoneNumber, email);
        repository.save(res);
        return res;
    }

    @Transactional
    @Override
    public void deleteCustomer(Integer id) {
        repository.delete(id);
    }

    @Override
    public List<Customer> findByName(String name) {
        return repository.findByName(name);
    }

    @Override
    public List<Customer> findBySurname(String surname) {
        return repository.findBySurname(surname);
    }

    @Override
    public List<Customer> findByNameAndSurname(String name, String surname) {
        return repository.findByNameAndSurname(name, surname);
    }

    @Override
    public Customer findBySocialSecurityNumber(Integer socialSecurityNumber) {
        return repository.findBySocialSecurityNumber(socialSecurityNumber);
    }

    @Override
    public Customer findByEmail(String email) {
        return repository.findByEmail(email);
    }

    @Override
    public List<Customer> listAllCustomers() {
        return repository.findAll();
    }

    @Transactional
    @Override
    public Customer updateCustomer(Integer id, String initials, String name, String surname, Date birthDate, Integer socialSecurityNumber, String address, String telephoneNumber, String email) {
        Customer res = repository.findOne(id);
        res.setInitials(initials);
        res.setName(name);
        res.setSurname(surname);
        res.setBirthDate(birthDate);
        res.setSocialSecurityNumber(socialSecurityNumber);
        res.setAddress(address);
        res.setTelephoneNumber(telephoneNumber);
        res.setEmail(email);
        repository.save(res);
        return res;
    }

    @Transactional
    @Override
    public Customer updateName(Integer id, String initials, String name, String surname) {
        Customer res = repository.findOne(id);
        res.setInitials(initials);
        res.setName(name);
        res.setSurname(surname);
        repository.save(res);
        return res;
    }

    @Transactional
    @Override
    public Customer updateAddress(Integer id, String address) {
        Customer res = repository.findOne(id);
        res.setAddress(address);
        repository.save(res);
        return res;
    }

    @Transactional
    @Override
    public Customer updateTelephoneNumber(Integer id, String telephoneNumber) {
        Customer res = repository.findOne(id);
        res.setTelephoneNumber(telephoneNumber);
        repository.save(res);
        return res;
    }

    @Transactional
    @Override
    public Customer updateEmail(Integer id, String email) {
        Customer res = repository.findOne(id);
        res.setEmail(email);
        repository.save(res);
        return res;
    }

}
