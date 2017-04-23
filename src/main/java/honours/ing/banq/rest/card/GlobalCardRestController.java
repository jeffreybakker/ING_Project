package honours.ing.banq.rest.card;

import honours.ing.banq.data.BankAccountService;
import honours.ing.banq.data.CardService;
import honours.ing.banq.data.CustomerService;
import honours.ing.banq.model.BankAccount;
import honours.ing.banq.model.Card;
import honours.ing.banq.model.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Kevin Witlox
 */
@RestController
@RequestMapping("/api/card")
public class GlobalCardRestController {

    @Autowired
    private CardService cardService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private BankAccountService bankAccountService;

    @GetMapping("add")
    public Card addNewAccount(@RequestParam int holderId, @RequestParam int bankAccountId) {
        Customer cardHolder = customerService.findCustomerById(holderId);
        if (cardHolder == null) {
            return null;
        }

        List<Customer> holders = new ArrayList<>();
        holders.add(cardHolder);

        BankAccount bankAccount = bankAccountService.findAccountById(bankAccountId);

        Card res = new Card(holders, bankAccount);
        return cardService.save(res);
    }

    @GetMapping("/list")
    @ResponseBody
    public Iterable<Card> listCards() {
        return cardService.findAll();
    }

}
