package honours.ing.banq.card;

import com.googlecode.jsonrpc4j.JsonRpcParam;
import com.googlecode.jsonrpc4j.JsonRpcService;

import java.util.List;

/**
 * @author jeffrey
 * @since 24-4-17
 */
@JsonRpcService("/api/card")
public interface CardService {

    Card createCard(@JsonRpcParam("holderId") Integer holderId, @JsonRpcParam("accountId") Integer accountId);

    void deleteCard(@JsonRpcParam("id") Integer id);

    Card findByHolderAndAccount(@JsonRpcParam("holderId") Integer holderId, @JsonRpcParam("accountId") Integer accountId);
    List<Card> findByHolder(@JsonRpcParam("holderId") Integer holderId);
    List<Card> findByAccount(@JsonRpcParam("accountId") Integer accountId);

}
