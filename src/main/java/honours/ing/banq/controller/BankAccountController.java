package honours.ing.banq.controller;

import honours.ing.banq.model.BankAccount;
import honours.ing.banq.data.BankAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author Jeffrey Bakker
 */
@RestController
@RequestMapping("/api/account")
public class BankAccountController {

    @Autowired
    private BankAccountRepository accountRepository;

//    @GetMapping("/add")
//    @ResponseBody
//    public String addNewAccount(@RequestParam int holder, @RequestParam double balance) {
//        BankAccount res = new BankAccount(holder, balance);
//        accountRepository.save(res);
//        return "Saved";
//    }

    @GetMapping("/list")
    @ResponseBody
    public Iterable<BankAccount> listAccounts() {
        return accountRepository.findAll();
    }

}
