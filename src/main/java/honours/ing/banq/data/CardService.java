package honours.ing.banq.data;

import honours.ing.banq.model.BankAccount;
import honours.ing.banq.model.Card;
import honours.ing.banq.model.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Kevin Witlox on 16-4-2017.
 */
@Repository
@Transactional(readOnly = true)
public class CardService {

    @Autowired
    private CardRepository repository;

    @Transactional
    public void remove(Card card) { repository.delete(card); }

    @Transactional
    public Card save(Card card) { return repository.save(card); }

    public List<Card> findCardByHolderAndAccount(Customer holder, BankAccount account) {
        return repository.findByHolderAndAccount(holder, account);
    }

    public Card findAccountById(int id) { return repository.findOne(id); }

    public List<Card> findAll() { return repository.findAll(); }

}
