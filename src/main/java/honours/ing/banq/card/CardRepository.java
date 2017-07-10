package honours.ing.banq.card;

import honours.ing.banq.account.BankAccount;
import honours.ing.banq.customer.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author Kevin Witlox
 */
public interface CardRepository extends JpaRepository<Card, Integer> {

    List<Card> findByAccount(BankAccount account);
    Card findByAccountAndHolder(BankAccount account, Customer holder);
    Card findByAccountAndCardNumber(BankAccount account, String cardNumber);
    Card findByCardNumber(String cardNumber);

}
