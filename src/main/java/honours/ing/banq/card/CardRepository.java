package honours.ing.banq.card;

import honours.ing.banq.account.model.BankAccount;
import honours.ing.banq.customer.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * A repository for the {@link Card} entity.
 * @author Kevin Witlox
 */
public interface CardRepository extends JpaRepository<Card, Integer> {

    /**
     * Returns a list of cards corresponding to the given {@link BankAccount}.
     * @param account the {@link BankAccount} to find all cards for
     * @return a list of {@link Card}s
     */
    List<Card> findByAccount(BankAccount account);

    /**
     * Returns all cards for a given combination of {@link BankAccount} and {@link Customer}.
     * @param account the account to find all cards for
     * @param holder the holder to find all cards for
     * @return a list of {@link Card}s
     */
    List<Card> findByAccountAndHolder(BankAccount account, Customer holder);

    /**
     * Returns the card with the given card number for the given {@link BankAccount}.
     * @param account    the {@link BankAccount}
     * @param cardNumber the card number
     * @return the card with the given number for the given {@link BankAccount}
     */
    Card findByAccountAndCardNumber(BankAccount account, String cardNumber);

    @Deprecated
    Card findByCardNumber(String cardNumber);

}
