package honours.ing.banq.card;

import com.googlecode.jsonrpc4j.JsonRpcParam;
import com.googlecode.jsonrpc4j.JsonRpcService;

import java.util.List;

/**
 * @author Jeffrey Bakker
 * @since 24-4-17
 */
@JsonRpcService("/api/card")
public interface CardService {

    /**
     * Creates a new card with the given parameters.
     * @param holderId the id of the holder of the card
     * @param accountId the id of the bank account that the card has access to
     * @return the newly created card
     */
    Card createCard(@JsonRpcParam("holderId") Integer holderId, @JsonRpcParam("accountId") Integer accountId);

    /**
     * Deletes a card with the given id.
     * @param id the id of the card to be removed
     */
    void deleteCard(@JsonRpcParam("id") Integer id);

    /**
     * Returns the card corresponding to the given parameters.
     * @param holderId the id of the holder of the card
     * @param accountId the id of the account the card has access to
     * @return the card
     */
    Card findByHolderAndAccount(@JsonRpcParam("holderId") Integer holderId, @JsonRpcParam("accountId") Integer accountId);

    /**
     * Returns a list of cards owned by the holder with the given id.
     * @param holderId the id of the holder to find all the cards for
     * @return a list of cards
     */
    List<Card> findByHolder(@JsonRpcParam("holderId") Integer holderId);

    /**
     * Returns a list of cards that have access to the bank account with the given account id.
     * @param accountId the id of the account to find all the cards for
     * @return a list of cards
     */
    List<Card> findByAccount(@JsonRpcParam("accountId") Integer accountId);

}
