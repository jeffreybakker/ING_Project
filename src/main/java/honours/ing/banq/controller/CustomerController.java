package honours.ing.banq.controller;

import honours.ing.banq.model.Customer;
import honours.ing.banq.data.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * @author Jeffrey Bakker
 */
@RestController
@RequestMapping("/api/customer")
public class CustomerController {

    @Autowired
    private CustomerRepository customerRepository;

    @GetMapping("/add")
    @ResponseBody
    public String addNewCustomer(@RequestParam String initials, @RequestParam String name, @RequestParam String surname, @RequestParam @DateTimeFormat(pattern="dd-MM-yyyy") Date birthDate, @RequestParam Integer socialSecurityNumber, @RequestParam String address, @RequestParam String telephoneNumber, @RequestParam String email) {
        Customer res = new Customer(initials, name, surname, birthDate, socialSecurityNumber, address, telephoneNumber, email);
        customerRepository.save(res);
        return "Saved";
    }

    @GetMapping("/find")
    public Customer findCustomer(@RequestParam int id) {
        return customerRepository.findOne((long) id);
    }

    @GetMapping("/list")
    @ResponseBody
    public Iterable<Customer> listCustomers() {
        return customerRepository.findAll();
    }

}
