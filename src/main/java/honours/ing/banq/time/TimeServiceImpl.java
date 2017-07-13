package honours.ing.banq.time;

import com.googlecode.jsonrpc4j.spring.AutoJsonRpcServiceImpl;
import honours.ing.banq.access.NoEffectError;
import honours.ing.banq.account.BankAccount;
import honours.ing.banq.account.BankAccountRepository;
import honours.ing.banq.time.bean.DateBean;
import honours.ing.banq.transaction.Transaction;
import honours.ing.banq.transaction.TransactionRepository;
import honours.ing.banq.util.IBANUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
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
    private TransactionRepository transactionRepository;

    @Autowired
    private BankAccountRepository bankAccountRepository;

    @Override
    public void simulateTime(int nrOfDays) {
        // Delete previous
        List<Time> times = timeRepository.findAll();
        if (times.size() != 1) {
            throw new IllegalStateException("There should only be one time entry in the database.");
        }
        timeRepository.delete(times);

        // Save new
        timeRepository.save(new Time(nrOfDays));
    }

    @Override
    public void reset() throws NoEffectError {
        // Delete previous entry
        List<Time> times = timeRepository.findAll();
        timeRepository.delete(times);
        // Reset time to current time
        Time time = new Time(0);
        timeRepository.save(time);

        // Revert transactions
        List<Transaction> futureTransactions = transactionRepository.findAllByDateAfter(getDate().getDate());
        for (Transaction transaction : futureTransactions) {
            BankAccount source = bankAccountRepository.findOne((int) IBANUtil.getAccountNumber(transaction.getSource
                    ()));
            BankAccount destination = bankAccountRepository.findOne((int) IBANUtil.getAccountNumber(transaction
                    .getDestination()));
            if (source != null) {
                source.addBalance(transaction.getAmount());
                bankAccountRepository.save(source);
            }

            destination.subBalance(transaction.getAmount());
            bankAccountRepository.save(destination);
        }
        transactionRepository.delete(futureTransactions);
    }

    @Override
    public DateBean getDate() {
        List<Time> times = timeRepository.findAll();
        if (times.size() != 1) {
            throw new IllegalStateException("There should only be one time entry in the database.");
        }

        Date date = new Date();
        long milisec = date.getTime() + times.get(0).getShift() * 24 * 60 * 60 * 1000;
        date.setTime(milisec);
        return new DateBean(date);
    }

}
