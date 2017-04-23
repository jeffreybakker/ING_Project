package honours.ing.banq.rest.card;

import honours.ing.banq.data.BankAccountService;
import honours.ing.banq.data.CardService;
import honours.ing.banq.data.CustomerService;
import honours.ing.banq.model.Card;
import honours.ing.banq.model.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

/**
 * @author Kevin Witlox
 */
@RestController
@RequestMapping("api/card/{cardId}")
public class DetailedCardRestController {

    @Autowired
    private CardService cardService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private BankAccountService bankAccountService;

    @GetMapping
    public Card getInfo(@PathVariable int cardId) {
        return cardService.findAccountById(cardId);
    }

    @GetMapping("/addHolder")
    @ResponseBody
    public String addHolder(@PathVariable int cardId, @RequestParam int holderId) {
        Customer cardHolder = customerService.findCustomerById(holderId);
        if (cardHolder == null) {
            return "Holder not found";
        }

        Card card = cardService.findAccountById(cardId);
        if (card == null) {
            return "Card not found";
        }

        card.addHolder(cardHolder);
        cardService.save(card);

        return "Saved";
    }

    @GetMapping("/listHolders")
    @ResponseBody
    public Iterable<Customer> listHolders(@PathVariable int cardId) {
        Card card = cardService.findAccountById(cardId);
        if (card == null) {
            return new ArrayList<>();
            // TODO: NOT FOUND
        }

        return card.getHolders();
    }

    @GetMapping("/removeHolder")
    @ResponseBody
    public String removeHolder(@PathVariable int cardId, @RequestParam int holderId) {
        Customer accountHolder = customerService.findCustomerById(holderId);
        if (accountHolder == null) {
            return "Holder not found";
        }

        Card card = cardService.findAccountById(cardId);
        if (card == null) {
            return "Bank Account not found";
        }

        card.removeHolder(accountHolder);
        cardService.save(card);

        return "Saved";
    }

    @GetMapping("/remove")
    @ResponseBody
    public String remove(@PathVariable int cardId) {
        Card card = cardService.findAccountById(cardId);
        if (card == null) {
            return "Card not found";
        }

        cardService.remove(card);

        return "Removed";
    }

}
