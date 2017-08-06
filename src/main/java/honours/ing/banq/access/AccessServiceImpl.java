package honours.ing.banq.access;

import com.googlecode.jsonrpc4j.spring.AutoJsonRpcServiceImpl;
import honours.ing.banq.InvalidParamValueError;
import honours.ing.banq.access.bean.NewCardBean;
import honours.ing.banq.account.BankAccount;
import honours.ing.banq.account.BankAccountRepository;
import honours.ing.banq.auth.AuthService;
import honours.ing.banq.auth.NotAuthorizedError;
import honours.ing.banq.card.Card;
import honours.ing.banq.card.CardRepository;
import honours.ing.banq.card.CardUtil;
import honours.ing.banq.customer.Customer;
import honours.ing.banq.customer.CustomerRepository;
import honours.ing.banq.log.LogService;
import honours.ing.banq.util.IBANUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * An implementation of the {@link AccessService}.
 * @author Jeffrey Bakker
 * @since 31-5-17
 */
@Service
@AutoJsonRpcServiceImpl
@Transactional
public class AccessServiceImpl implements AccessService {

    // Services
    @Autowired
    private AuthService auth;

    // Repositories
    @Autowired
    private BankAccountRepository accountRepository;

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private LogService log;

    @Transactional
    @Override
    public NewCardBean provideAccess(String authToken, String iBAN, String username) throws InvalidParamValueError, NotAuthorizedError, NoEffectError {
        Customer customer = auth.getAuthorizedCustomer(authToken);

        long accountNumber = IBANUtil.getAccountNumber(iBAN);
        BankAccount account = accountRepository.findOne((int) accountNumber);

        if (account == null) {
            log.e(String.format(
                    "%s tried to give %s access to non-existing account %s",
                    customer.getUsername(), username, iBAN));
            throw new InvalidParamValueError("Account does not exist");
        }

        if (!account.getPrimaryHolder().equals(customer)) {
            log.e(String.format(
                    "%s tried to give %s access to account %s over which he/she has no authorisation",
                    customer.getUsername(), username, iBAN));
            throw new NotAuthorizedError();
        }

        Customer holder = customerRepository.findByUsername(username);

        if (holder == null) {
            log.e(String.format(
                    "%s tried to give non-existing user %s access to account %s",
                    customer.getUsername(), username, iBAN));
            throw new InvalidParamValueError("The given username does not exist.");
        }

        if (account.getPrimaryHolder().equals(holder)) {
            log.w(String.format(
                    "%s tried to give him/herself access to his/her own account %s",
                    username, iBAN));
            throw new InvalidParamValueError("You can't provide access to yourself for your own account");
        }

        if (account.getHolders().contains(holder)) {
            log.w(String.format(
                    "%s tried to give %s access to account %s to which he/she already had access",
                    customer.getUsername(), username, iBAN));
            throw new NoEffectError();
        }

        account.addHolder(holder);
        accountRepository.save(account);
        Card card = new Card(holder, account, CardUtil.generateCardNumber(cardRepository));
        cardRepository.save(card);

        return new NewCardBean(card);
    }

    @Transactional
    @Override
    public void revokeAccess(String authToken, String iBAN, String username) throws InvalidParamValueError, NotAuthorizedError, NoEffectError {
        Customer customer = auth.getAuthorizedCustomer(authToken);

        long accountNumber = IBANUtil.getAccountNumber(iBAN);
        BankAccount account = accountRepository.findOne((int) accountNumber);

        if (!account.getPrimaryHolder().equals(customer)) {
            log.e(String.format(
                    "%s tried to revoke access from %s for %s, but he had no authorisation",
                    customer.getUsername(), iBAN, username));
            throw new NotAuthorizedError();
        }

        Customer holder = customerRepository.findByUsername(username);

        if (holder == null) {
            log.e(String.format(
                    "%s tried to revoke access from %s for non-existing user %s",
                    customer.getUsername(), iBAN, username));
            throw new InvalidParamValueError("The given username does not exist.");
        }

        if (account.getPrimaryHolder().equals(holder)) {
            log.w(String.format(
                    "%s tried to revoke his own access to his own account %s",
                    customer.getUsername(), iBAN));
            throw new InvalidParamValueError("You can't revoke access from yourself for your own account");
        }

        if (!account.getHolders().contains(holder)) {
            log.w(String.format(
                    "%s tried to revoke access from %s for non-holder %s",
                    customer.getUsername(), iBAN, username));
            throw new NoEffectError();
        }

        account.removeHolder(holder);
        accountRepository.save(account);
        Card card = cardRepository.findByAccountAndHolder(account, holder);
        cardRepository.delete(card);
    }

}
