package honours.ing.banq.rest.customer;

import honours.ing.banq.data.BankAccountService;
import honours.ing.banq.data.CustomerService;
import honours.ing.banq.model.BankAccount;
import honours.ing.banq.model.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author jeffrey
 * @since 29-3-17
 */
@RestController
@RequestMapping("/api/customer/{customerId}")
public class DetailedCustomerRestController {

    @Autowired
    private BankAccountService accountService;

    @Autowired
    private CustomerService customerService;

    @GetMapping
    public Customer getInfo(@PathVariable int customerId) {
        return customerService.findCustomerById(customerId);
    }

    @GetMapping("/accounts")
    @ResponseBody
    public Iterable<BankAccount> getAccounts(@PathVariable int customerId) {
        return accountService.findAccountsByHolder(customerService.findCustomerById(customerId));
    }

    @GetMapping("/remove")
    @ResponseBody
    public String remove(@PathVariable int customerId) {
        customerService.remove(customerService.findCustomerById(customerId));
        return "Removed";
    }

}
