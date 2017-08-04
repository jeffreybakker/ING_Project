package honours.ing.banq.card;

import com.googlecode.jsonrpc4j.spring.AutoJsonRpcServiceImpl;
import honours.ing.banq.InvalidParamValueError;
import honours.ing.banq.access.NoEffectError;
import honours.ing.banq.account.BankAccount;
import honours.ing.banq.account.BankAccountRepository;
import honours.ing.banq.auth.AuthService;
import honours.ing.banq.auth.InvalidPINError;
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

    @Transactional
    @Override
    public Object unblockCard(String authToken, String iBAN, String pinCard)
            throws InvalidParamValueError, NotAuthorizedError, NoEffectError, InvalidPINError {
        Card card = auth.getAuthorizedCard(authToken, iBAN, pinCard);

        if (!card.isBlocked()) {
            throw new NoEffectError();
        }

        card.resetAttempts();
        repository.save(card);

        return new Object();
    }
}
