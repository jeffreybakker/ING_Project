package honours.ing.banq.rest.account;

import honours.ing.banq.data.BankAccountService;
import honours.ing.banq.data.CustomerService;
import honours.ing.banq.model.BankAccount;
import honours.ing.banq.model.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jeffrey
 * @since 29-3-17
 */
@RestController
@RequestMapping("/api/account")
public class GlobalBankAccountRestController {

    @Autowired
    private BankAccountService accountService;

    @Autowired
    private CustomerService customerService;

    @GetMapping("/add")
    public BankAccount addNewAccount(@RequestParam int holder, @RequestParam double balance) {
        Customer accountHolder = customerService.findCustomerById(holder);
        if (accountHolder == null) {
            return null;
        }

        List<Customer> holders = new ArrayList<>();
        holders.add(accountHolder);

        BankAccount res = new BankAccount(holders, balance);
        return accountService.save(res);
    }

    @GetMapping("/list")
    @ResponseBody
    public Iterable<BankAccount> listAccounts() {
        return accountService.findAll();
    }

}
