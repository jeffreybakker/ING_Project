package honours.ing.banq.card;

import com.googlecode.jsonrpc4j.spring.AutoJsonRpcServiceImpl;
import honours.ing.banq.error.InvalidParamValueError;
import honours.ing.banq.error.NoEffectError;
import honours.ing.banq.access.bean.NewCardBean;
import honours.ing.banq.auth.AuthService;
import honours.ing.banq.error.InvalidPINError;
import honours.ing.banq.error.NotAuthorizedError;
import honours.ing.banq.transaction.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * An implementation for the {@link CardService}.
 * @author Jeffrey Bakker
 */
@Service
@AutoJsonRpcServiceImpl
@Transactional(readOnly = true)
public class CardServiceImpl implements CardService {

    // Services
    private AuthService auth;
    private TransactionService transactionService;

    // Repositories
    private CardRepository repository;

    @Autowired
    public CardServiceImpl(AuthService auth, TransactionService transactionService, CardRepository repository) {
        this.auth = auth;
        this.transactionService = transactionService;
        this.repository = repository;
    }

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

    @Override
    public NewCardBean invalidateCard(String authToken, String iBAN, String pinCard, boolean newPin)
            throws InvalidParamValueError, NotAuthorizedError, InvalidPINError, NoEffectError {
        Card old = auth.getAuthorizedCard(authToken, iBAN, pinCard);

        if (old.isInvalidated()) {
            throw new NoEffectError();
        }

        old.setInvalidated(true);

        Card res;

        if (newPin) {
            res = new Card(old.getHolder(), old.getAccount(), CardUtil.generateCardNumber(repository));
        } else {
            res = new Card(old.getHolder(), old.getAccount(), CardUtil.generateCardNumber(repository), old.getPin());
        }

        repository.save(old);
        repository.save(res);

        transactionService.forceTransactionAccount(
                res.getAccount().getCheckingAccount(), new BigDecimal("-7.50"), "New PIN card");

        return new NewCardBean(res);
    }
}
