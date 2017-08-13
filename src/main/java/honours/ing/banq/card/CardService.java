package honours.ing.banq.card;

import com.googlecode.jsonrpc4j.JsonRpcParam;
import com.googlecode.jsonrpc4j.JsonRpcService;
import honours.ing.banq.InvalidParamValueError;
import honours.ing.banq.access.NoEffectError;
import honours.ing.banq.access.bean.NewCardBean;
import honours.ing.banq.auth.InvalidPINError;
import honours.ing.banq.auth.NotAuthorizedError;

/**
 * @author jeffrey
 * @since 3-8-17
 */
@JsonRpcService("/api/card")
public interface CardService {

    /**
     * A PIN card that has been blocked can be unblocked if the user logs in and calls this unblock card method.
     * @param authToken the authentication token, obtained with {@code getAuthToken()}
     * @param iBAN      the number of the bank account
     * @param pinCard   the number of the pin card
     * @return an empty dictionary if successful
     * @throws InvalidParamValueError one or more parameter has an invalid value. See the message
     * @throws NotAuthorizedError the authenticated user is not authorized to perform this action
     * @throws NoEffectError if the card is not blocked this method will have no effect
     */
    Object unblockCard(@JsonRpcParam("authToken") String authToken, @JsonRpcParam("iBAN") String iBAN,
                       @JsonRpcParam("pinCard") String pinCard)
            throws InvalidParamValueError, NotAuthorizedError, NoEffectError, InvalidPINError;

    /**
     * A PIN card can be invalidated if it is lost and replaced with a new card by using this method.
     * @param authToken the authentication token
     * @param iBAN      the number of the bank account
     * @param pinCard   the number of the pin card
     * @param newPin    {@code true} if the user wishes to get a new PIN code
     * @return an dictionary containing the new information
     * @throws InvalidParamValueError if one or more parameters are invalid
     * @throws NotAuthorizedError if the user is not authorized to do this
     * @throws InvalidPINError if the card is not valid
     */
    NewCardBean invalidateCard(@JsonRpcParam("authToken") String authToken, @JsonRpcParam("iBAN") String iBAN,
                               @JsonRpcParam("pinCard") String pinCard, @JsonRpcParam("newPin") boolean newPin)
            throws InvalidParamValueError, NotAuthorizedError, InvalidPINError, NoEffectError;

}
