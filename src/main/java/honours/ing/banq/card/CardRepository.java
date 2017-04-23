package honours.ing.banq.data;

import honours.ing.banq.model.BankAccount;
import honours.ing.banq.card.Card;
import honours.ing.banq.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author Kevin Witlox
 */
public interface CardRepository extends JpaRepository<Card, Integer> {

    List<Card> findByHolderAndAccount(Customer holder, BankAccount account);

}
