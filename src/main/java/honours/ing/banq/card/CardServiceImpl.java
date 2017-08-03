package honours.ing.banq.card;

import com.googlecode.jsonrpc4j.spring.AutoJsonRpcServiceImpl;
import honours.ing.banq.InvalidParamValueError;
import honours.ing.banq.access.NoEffectError;
import honours.ing.banq.account.BankAccount;
import honours.ing.banq.account.BankAccountRepository;
import honours.ing.banq.auth.AuthService;
import honours.ing.banq.auth.NotAuthorizedError;
import honours.ing.banq.customer.Customer;
import honours.ing.banq.util.IBANUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AutoJsonRpcServiceImpl
@Transactional(readOnly = true)
public class CardServiceImpl implements CardService {

    // Services
    @Autowired
    private AuthService auth;

    // Repositories
    @Autowired
    private CardRepository repository;

    @Autowired
    private BankAccountRepository accountRepository;

    @Transactional
    @Override
    public Object unblockCard(String authToken, String iBAN, String pinCard)
            throws InvalidParamValueError, NotAuthorizedError, NoEffectError {
        Customer customer = auth.getAuthorizedCustomer(authToken);

        // Retrieve the bank account and check whether we are authorized to access it
        BankAccount account = accountRepository.findOne((int) IBANUtil.getAccountNumber(iBAN));
        if (account == null) {
            throw new InvalidParamValueError("There is no bank account with the given iBAN");
        }

        if (account.getPrimaryHolder() != customer && !account.getHolders().contains(customer)) {
            throw new NotAuthorizedError();
        }

        // Retrieve the pin card and check whether we are authorized to access it
        Card card = repository.findByAccountAndCardNumber(account, pinCard);
        if (card == null) {
            throw new InvalidParamValueError("There is no pin card with the given card number");
        }

        if (card.getHolder() != customer) {
            throw new NotAuthorizedError();
        }

        if (!card.isBlocked()) {
            throw new NoEffectError();
        }

        card.resetAttempts();
        repository.save(card);

        return new Object();
    }
}
