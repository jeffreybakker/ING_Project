package honours.ing.banq.card;

import honours.ing.banq.account.BankAccount;
import honours.ing.banq.customer.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author Kevin Witlox
 */
public interface CardRepository extends JpaRepository<Card, Integer> {

    /**
     * Returns a card that matches the given parameters.
     * @param holder the holder of the card
     * @param account the account that the card has access to
     * @return the card corresponding to the given parameters
     */
    Card findByHolderAndAccount(Customer holder, BankAccount account);

    /**
     * Returns all cards of the given holder.
     * @param holder the holder
     * @return a list of cards
     */
    List<Card> findByHolder(Customer holder);

    /**
     * Returns all cards that have access to the given bank account.
     * @param account the bank account
     * @return a list of cards
     */
    List<Card> findByAccount(BankAccount account);

}
