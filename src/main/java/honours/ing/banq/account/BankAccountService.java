package honours.ing.banq.account;

import com.fasterxml.jackson.annotation.JsonView;
import com.googlecode.jsonrpc4j.JsonRpcParam;
import com.googlecode.jsonrpc4j.JsonRpcService;
import honours.ing.banq.InvalidParamValueError;
import honours.ing.banq.View;
import honours.ing.banq.card.Card;
import honours.ing.banq.customer.Customer;

import java.util.Date;
import java.util.List;

/**
 * @author jeffrey
 * @since 17-4-17
 */
@JsonRpcService("/api")
public interface BankAccountService {



}
