package honours.ing.banq.info;

import com.googlecode.jsonrpc4j.spring.AutoJsonRpcServiceImpl;
import honours.ing.banq.error.InvalidParamValueError;
import honours.ing.banq.account.model.BankAccount;
import honours.ing.banq.account.BankAccountRepository;
import honours.ing.banq.auth.AuthService;
import honours.ing.banq.error.NotAuthorizedError;
import honours.ing.banq.customer.Customer;
import honours.ing.banq.info.bean.BalanceBean;
import honours.ing.banq.info.bean.BankAccountAccessBean;
import honours.ing.banq.info.bean.UserAccessBean;
import honours.ing.banq.transaction.Transaction;
import honours.ing.banq.transaction.TransactionRepository;
import honours.ing.banq.util.IBANUtil;
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
@Transactional
public class InfoServiceImpl implements InfoService {

    // Services
    private AuthService auth;

    // Repositories
    private BankAccountRepository accountRepository;
    private TransactionRepository transactionRepository;

    @Autowired
    public InfoServiceImpl(AuthService auth,
                           BankAccountRepository accountRepository, TransactionRepository transactionRepository) {
        this.auth = auth;
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    @Override
    public BalanceBean getBalance(String autToken, String iBAN) throws InvalidParamValueError, NotAuthorizedError {
        if (!IBANUtil.isValidIBAN(iBAN)) {
            throw new InvalidParamValueError("The given IBAN is invalid.");
        }

        Customer customer = auth.getAuthorizedCustomer(autToken);
        BankAccount bankAccount = accountRepository.findOne((int) IBANUtil.getAccountNumber(iBAN));

        if (bankAccount == null) {
            throw new InvalidParamValueError("The given IBAN does not exist.");
        }

        if (!bankAccount.getHolders().contains(customer) && !bankAccount.getPrimaryHolder().equals(customer)) {
            throw new NotAuthorizedError();
        }

        return new BalanceBean(bankAccount);
    }

    @Override
    public List<Transaction> getTransactionsOverview(String authToken, String iBAN, Integer nrOfTransactions)
            throws InvalidParamValueError, NotAuthorizedError {
        if (!IBANUtil.isValidIBAN(iBAN)) {
            throw new InvalidParamValueError("The given IBAN is invalid.");
        }

        Customer customer = auth.getAuthorizedCustomer(authToken);
        BankAccount bankAccount = accountRepository.findOne((int) IBANUtil.getAccountNumber(iBAN));

        if (bankAccount == null) {
            throw new InvalidParamValueError("The given IBAN does not exist.");
        }

        if (nrOfTransactions <= 0) {
            throw new InvalidParamValueError("The number of transactions should be positive.");
        }

        if (!bankAccount.getHolders().contains(customer) && !bankAccount.getPrimaryHolder().equals(customer)) {
            throw new NotAuthorizedError();
        }

        List<Transaction> list = transactionRepository.findBySourceOrDestinationOrderByDateDesc(iBAN, iBAN);
        list.addAll(transactionRepository.findBySourceOrDestinationOrderByDateDesc(iBAN + "S", iBAN + "S"));

        return list.size() > nrOfTransactions ?
                list.subList(0, nrOfTransactions)
                : transactionRepository.findBySourceOrDestinationOrderByDateDesc(iBAN, iBAN);
    }

    @Transactional
    @Override
    public List<UserAccessBean> getUserAccess(String authToken) throws NotAuthorizedError {
        Customer customer = auth.getAuthorizedCustomer(authToken);

        if (customer == null) {
            throw new NotAuthorizedError();
        }

        List<BankAccount> accounts = accountRepository.findBankAccountsByHolders(customer.getId());
        accounts.addAll(accountRepository.findBankAccountsByPrimaryHolder(customer.getId()));

        List<UserAccessBean> userAccessBeanList = new ArrayList<>();
        for (BankAccount account : accounts) {
            userAccessBeanList.add(new UserAccessBean(account, account.getPrimaryHolder()));
        }

        return userAccessBeanList;
    }

    @Override
    public List<BankAccountAccessBean> getBankAccountAccess(String authToken, String iBAN)
            throws InvalidParamValueError, NotAuthorizedError {
        Customer customer = auth.getAuthorizedCustomer(authToken);
        long accountNumber = IBANUtil.getAccountNumber(iBAN);
        BankAccount bankAccount = accountRepository.findOne((int) accountNumber);

        if (!bankAccount.getPrimaryHolder().equals(customer)) {
            throw new NotAuthorizedError();
        }

        // Add all account holders
        List<BankAccountAccessBean> bankAccountAccessBeanList = new ArrayList<>();
        for (Customer holder : bankAccount.getHolders()) {
            bankAccountAccessBeanList.add(new BankAccountAccessBean(holder));
        }

        // Add primary holder
        bankAccountAccessBeanList.add(new BankAccountAccessBean(customer));

        return bankAccountAccessBeanList;
    }

}
