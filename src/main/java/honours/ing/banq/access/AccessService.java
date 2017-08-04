package honours.ing.banq.access;

import com.googlecode.jsonrpc4j.JsonRpcParam;
import com.googlecode.jsonrpc4j.JsonRpcService;
import honours.ing.banq.InvalidParamValueError;
import honours.ing.banq.access.bean.NewCardBean;
import honours.ing.banq.auth.NotAuthorizedError;

/**
 * This service has methods for providing and revoking access to bank accounts for users other than the primary holder
 * of the account.
 * @author Jeffrey Bakker
 * @since 31-5-17
 */
@JsonRpcService("/api/access")
public interface AccessService {

    /**
     * A customer can share access to one of his bank accounts with another customer. This also creates a corresponding
     * PIN card.
     * @param authToken the authentication token, obtained with {@code getAuthToken}
     * @param iBAN the number of the bank account that will be shared
     * @param username the username of the customer that should get access
     * @return a bean with information about the new PIN card that was created if successful
     * @throws InvalidParamValueError one or more parameters have an invalid value, please see the message
     * @throws NotAuthorizedError the currently logged in user is not authorized to share access to this bank account
     * @throws NoEffectError this method has no effect because the other user already has access to this account
     */
    NewCardBean provideAccess(
            @JsonRpcParam("authToken") String authToken, @JsonRpcParam("iBAN") String iBAN,
            @JsonRpcParam("username") String username)
            throws InvalidParamValueError, NotAuthorizedError, NoEffectError;

    /**
     * A customer can revoke his access to a bank account if he is not the primary holder, otherwise the customer can
     * revoke another customers access.
     * @param authToken the authentication token, obtained with {@code getAuthToken}
     * @param iBAN the number of the bank account
     * @param username the username of the user whoms access should be revoked
     * @throws InvalidParamValueError
     * @throws NotAuthorizedError
     * @throws NoEffectError
     */
    void revokeAccess(
            @JsonRpcParam("authToken") String authToken, @JsonRpcParam("iBAN") String iBAN,
            @JsonRpcParam("username") String username)
            throws InvalidParamValueError, NotAuthorizedError, NoEffectError;



}
