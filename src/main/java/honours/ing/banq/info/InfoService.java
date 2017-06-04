package honours.ing.banq.info;

import com.googlecode.jsonrpc4j.JsonRpcParam;
import com.googlecode.jsonrpc4j.JsonRpcService;
import honours.ing.banq.account.BankAccount;
import honours.ing.banq.auth.NotAuthorizedError;
import honours.ing.banq.customer.Customer;
import honours.ing.banq.info.bean.UserAccessBean;

import java.util.List;

/**
 * @author Kevin Witlox
 */
@JsonRpcService("/api")
public interface InfoService {

    /**
     * Returns all {@link BankAccount} entities that can be accessed by the currently authorized {@link Customer}.
     *
     * @param authToken The authorization token to authorize and identify the {@link Customer}
     * @return A {@link List} of {@link UserAccessBean}
     * @throws NotAuthorizedError Thrown when something went wrong with the authentication module
     */
    List<UserAccessBean> getUserAcces(@JsonRpcParam("authToken") String authToken ) throws NotAuthorizedError;

}
