package honours.ing.banq.account;

import com.fasterxml.jackson.annotation.JsonView;
import com.googlecode.jsonrpc4j.spring.AutoJsonRpcServiceImpl;
import honours.ing.banq.InvalidParamValueError;
import honours.ing.banq.View;
import honours.ing.banq.card.Card;
import honours.ing.banq.customer.Customer;
import honours.ing.banq.customer.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author jeffrey
 * @since 17-4-17
 */
@Service
@AutoJsonRpcServiceImpl
@Transactional(readOnly = true)
public class BankAccountServiceImpl implements BankAccountService {
}
