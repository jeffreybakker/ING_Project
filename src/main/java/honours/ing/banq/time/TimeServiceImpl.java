package honours.ing.banq.time;

import com.googlecode.jsonrpc4j.spring.AutoJsonRpcServiceImpl;
import honours.ing.banq.access.NoEffectError;
import honours.ing.banq.account.BankAccount;
import honours.ing.banq.account.BankAccountRepository;
import honours.ing.banq.card.CardRepository;
import honours.ing.banq.time.bean.DateBean;
import honours.ing.banq.transaction.Transaction;
import honours.ing.banq.transaction.TransactionRepository;
import honours.ing.banq.util.IBANUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * @author Kevin Witlox
 * @since 13-7-2017.
 */
@Service
@AutoJsonRpcServiceImpl
@Transactional
public class TimeServiceImpl implements TimeService {

    // Repositories
    @Autowired
    private TimeRepository timeRepository;

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private BankAccountRepository bankAccountRepository;

    @Override
    public void simulateTime(int nrOfDays) {

    }

    @Override
    public void reset() throws NoEffectError {

    }

    @Override
    public DateBean getDate() {
        return null;
    }

}
