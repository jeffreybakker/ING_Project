package honours.ing.banq.info;

import com.googlecode.jsonrpc4j.spring.AutoJsonRpcServiceImpl;
import honours.ing.banq.account.BankAccount;
import honours.ing.banq.account.BankAccountRepository;
import honours.ing.banq.auth.AuthService;
import honours.ing.banq.auth.NotAuthorizedError;
import honours.ing.banq.customer.Customer;
import honours.ing.banq.info.bean.UserAccessBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Kevin Witlox
 */
@Service
@AutoJsonRpcServiceImpl
@Transactional(readOnly = true)
public class InfoServiceImpl implements InfoService {

    @Autowired
    private AuthService auth;

    @Autowired
    private BankAccountRepository bankAccountRepository;

    @Transactional
    @Override
    public List<UserAccessBean> getUserAcces(String authToken) throws NotAuthorizedError {
        Customer customer = auth.getAuthorizedCustomer(authToken);
        List<BankAccount> accounts = bankAccountRepository.findBankAccountsByHolders(customer);

        List<UserAccessBean> userAccessBeanList = new ArrayList<>();
        for (BankAccount account : accounts) {
            userAccessBeanList.add(new UserAccessBean(account, account.getPrimaryHolder()));
        }

        return userAccessBeanList;
    }

}
