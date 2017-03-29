package honours.ing.banq.rest.customer;

import honours.ing.banq.data.CustomerService;
import honours.ing.banq.model.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * @author jeffrey
 * @since 29-3-17
 */
@RestController
@RequestMapping("/api/customer")
public class GlobalCustomerRestController {

    @Autowired
    private CustomerService customerService;

    @GetMapping("/add")
    @ResponseBody
    public String addNewCustomer(@RequestParam String initials, @RequestParam String name, @RequestParam String surname, @RequestParam @DateTimeFormat(pattern="dd-MM-yyyy") Date birthDate, @RequestParam Integer socialSecurityNumber, @RequestParam String address, @RequestParam String telephoneNumber, @RequestParam String email) {
        Customer res = new Customer(initials, name, surname, birthDate, socialSecurityNumber, address, telephoneNumber, email);
        customerService.save(res);
        return "Saved";
    }

    @GetMapping(value = "/find", params = {"firstName"})
    @ResponseBody
    public Iterable<Customer> findCustomerByFirstName(@RequestParam String firstName) {
        return customerService.findCustomersByFirstName(firstName);
    }

    @GetMapping(value = "/find", params = {"surname"})
    @ResponseBody
    public Iterable<Customer> findCustomerBySurName(@RequestParam String surname) {
        return customerService.findCustomersBySurName(surname);
    }

    @GetMapping(value = "/find", params = {"firstName", "surname"})
    @ResponseBody
    public Iterable<Customer> findCustomerByName(@RequestParam String firstName, @RequestParam String surname) {
        return customerService.findCustomersByName(firstName, surname);
    }

    @GetMapping(value = "/find", params = {"socialSecurityNumber"})
    @ResponseBody
    public Customer findCustomerBySocialSecurityNumber(@RequestParam int socialSecurityNumber) {
        return customerService.findCustomerBySocialSecurityNumber(socialSecurityNumber);
    }

    @GetMapping(value = "/find", params = {"email"})
    @ResponseBody
    public Customer findCustomerByEmail(@RequestParam String email) {
        return customerService.findCustomerByEmail(email);
    }

    @GetMapping("/list")
    @ResponseBody
    public Iterable<Customer> listAccounts() {
        return customerService.findAll();
    }

}
