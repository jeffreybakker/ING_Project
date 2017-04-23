package honours.ing.banq.card;

import honours.ing.banq.account.BankAccount;
import honours.ing.banq.customer.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author Kevin Witlox
 */
public interface CardRepository extends JpaRepository<Card, Integer> {

    Card findByHolderAndAccount(Customer holder, BankAccount account);
    List<Card> findByHolder(Customer holder);
    List<Card> findByAccount(BankAccount account);

}
