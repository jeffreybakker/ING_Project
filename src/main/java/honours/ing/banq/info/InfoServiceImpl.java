package honours.ing.banq.info;

import com.googlecode.jsonrpc4j.spring.AutoJsonRpcServiceImpl;
import honours.ing.banq.InvalidParamValueError;
import honours.ing.banq.account.BankAccount;
import honours.ing.banq.account.BankAccountRepository;
import honours.ing.banq.auth.AuthService;
import honours.ing.banq.auth.NotAuthorizedError;
import honours.ing.banq.customer.Customer;
import honours.ing.banq.info.bean.BalanceBean;
import honours.ing.banq.info.bean.BankAccountAccessBean;
import honours.ing.banq.info.bean.UserAccessBean;
import honours.ing.banq.transaction.Transaction;
import honours.ing.banq.transaction.TransactionRepository;
import honours.ing.banq.util.IBANUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.print.Pageable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Kevin Witlox
 */
@Service
@AutoJsonRpcServiceImpl
@Transactional(readOnly = true)
public class InfoServiceImpl implements InfoService {

    // Services
    @Autowired
    private AuthService auth;

    // Repositories
    @Autowired
    private BankAccountRepository bankAccountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Override
    public BalanceBean getBalance(String autToken, String iBAN) throws InvalidParamValueError,
            NotAuthorizedError {
        Customer customer = auth.getAuthorizedCustomer(autToken);
        BankAccount bankAccount = bankAccountRepository.findOne((int) IBANUtil.getAccountNumber
                (iBAN));

        if (!bankAccount.getHolders().contains(customer)) {
            throw new NotAuthorizedError();
        }

        return new BalanceBean(bankAccount);
    }

    @Override
    public List<Transaction> getTransactionsOverview(String authToken, String iBAN, Integer
            nrOfTransactions) throws InvalidParamValueError, NotAuthorizedError {
        Customer customer = auth.getAuthorizedCustomer(authToken);
        BankAccount bankAccount = bankAccountRepository.findOne((int) IBANUtil.getAccountNumber
                (iBAN));

        if (!bankAccount.getHolders().contains(customer)) {
            throw new NotAuthorizedError();
        }

        Pageable pageable = (Pageable) new PageRequest(0, nrOfTransactions);
        return transactionRepository.findBySourceOrDestinationOrderByDateDesc(iBAN, pageable);
    }

    @Transactional
    @Override
    public List<UserAccessBean> getUserAcces(String authToken) throws NotAuthorizedError {
        Customer customer = auth.getAuthorizedCustomer(authToken);

        if (customer == null) {
            throw new NotAuthorizedError();
        }

        List<BankAccount> accounts = bankAccountRepository.findBankAccountsByHolders(customer);

        List<UserAccessBean> userAccessBeanList = new ArrayList<>();
        for (BankAccount account : accounts) {
            userAccessBeanList.add(new UserAccessBean(account, account.getPrimaryHolder()));
        }

        return userAccessBeanList;
    }

    @Override
    public List<BankAccountAccessBean> getBankAccountAccess(String authToken, String iBAN) throws
            InvalidParamValueError, NotAuthorizedError {
        Customer customer = auth.getAuthorizedCustomer(authToken);
        long accountNumber = IBANUtil.getAccountNumber(iBAN);
        BankAccount bankAccount = bankAccountRepository.findOne((int) accountNumber);

        if (!bankAccount.getPrimaryHolder().equals(customer)) {
            throw new NotAuthorizedError();
        }

        List<BankAccountAccessBean> bankAccountAccessBeanList = new ArrayList<>();
        for (Customer holder : bankAccount.getHolders()) {
            bankAccountAccessBeanList.add(new BankAccountAccessBean(holder));
        }

        return bankAccountAccessBeanList;
    }

}
