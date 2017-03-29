package honours.ing.banq.rest.account;

import honours.ing.banq.data.BankAccountService;
import honours.ing.banq.data.CustomerService;
import honours.ing.banq.model.BankAccount;
import honours.ing.banq.model.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

/**
 * @author jeffrey
 * @since 29-3-17
 */
@RestController
@RequestMapping("/api/account/{accountId}")
public class DetailedBankAccountRestController {

    @Autowired
    private BankAccountService accountService;

    @Autowired
    private CustomerService customerService;

    @GetMapping
    public BankAccount getInfo(@PathVariable int accountId) {
        return accountService.findAccountById(accountId);
    }

    @GetMapping("/addHolder")
    @ResponseBody
    public String addHolder(@PathVariable int accountId, @RequestParam int holderId) {
        Customer accountHolder = customerService.findCustomerById(holderId);
        if (accountHolder == null) {
            return "Holder not found";
        }

        BankAccount bankAccount = accountService.findAccountById(accountId);
        if (bankAccount == null) {
            return "Bank Account not found";
        }

        bankAccount.addHolder(accountHolder);
        accountService.save(bankAccount);

        return "Saved";
    }

    @GetMapping("/listHolders")
    @ResponseBody
    public Iterable<Customer> listHolders(@PathVariable int accountId) {
        BankAccount bankAccount = accountService.findAccountById(accountId);
        if (bankAccount == null) {
            return new ArrayList<>();
            // TODO: NOT FOUND
        }

        return bankAccount.getHolders();
    }

    @GetMapping("/removeHolder")
    @ResponseBody
    public String removeHolder(@PathVariable int accountId, @RequestParam int holderId) {
        Customer accountHolder = customerService.findCustomerById(holderId);
        if (accountHolder == null) {
            return "Holder not found";
        }

        BankAccount bankAccount = accountService.findAccountById(accountId);
        if (bankAccount == null) {
            return "Bank Account not found";
        }

        bankAccount.removeHolder(accountHolder);
        accountService.save(bankAccount);

        return "Saved";
    }

    @GetMapping("/remove")
    @ResponseBody
    public String remove(@PathVariable int accountId) {
        BankAccount bankAccount = accountService.findAccountById(accountId);
        if (bankAccount == null) {
            return "Bank Account not found";
        }

        accountService.remove(bankAccount);

        return "Removed";
    }

    @GetMapping("/transfer")
    @ResponseBody
    public String transfer(@PathVariable int accountId, @RequestParam int destId, @RequestParam double amount) {
        BankAccount sourceAccount = accountService.findAccountById(accountId);
        BankAccount destAccount = accountService.findAccountById(destId);

        sourceAccount.setBalance(sourceAccount.getBalance() - amount);
        destAccount.setBalance(destAccount.getBalance() + amount);

        accountService.save(sourceAccount);
        accountService.save(destAccount);

        return "Transferred";
    }

}
