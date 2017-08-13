package honours.ing.banq.transaction;

import com.googlecode.jsonrpc4j.spring.AutoJsonRpcServiceImpl;
import honours.ing.banq.error.InvalidParamValueError;
import honours.ing.banq.account.model.Account;
import honours.ing.banq.account.model.BankAccount;
import honours.ing.banq.account.BankAccountRepository;
import honours.ing.banq.account.model.CheckingAccount;
import honours.ing.banq.auth.AuthService;
import honours.ing.banq.error.CardBlockedError;
import honours.ing.banq.error.InvalidPINError;
import honours.ing.banq.error.NotAuthorizedError;
import honours.ing.banq.time.TimeService;
import honours.ing.banq.util.IBANUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * An implementation for the {@link TransactionService}.
 * @author Kevin Witlox
 * @since 4-6-2017
 */
@Service
@AutoJsonRpcServiceImpl
@Transactional
public class TransactionServiceImpl implements TransactionService {

    // Services
    private AuthService auth;
    private TimeService timeService;

    // Repositories
    private BankAccountRepository accountRepository;
    private TransactionRepository transactionRepository;

    @Autowired
    public TransactionServiceImpl(AuthService auth, TimeService timeService, BankAccountRepository accountRepository,
                                  TransactionRepository transactionRepository) {
        this.auth = auth;
        this.timeService = timeService;
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    @Override
    public void depositIntoAccount(String iBAN, String pinCard, String pinCode, Double amount)
            throws InvalidParamValueError, InvalidPINError, CardBlockedError {
        if (!IBANUtil.isValidIBAN(iBAN)) {
            throw new InvalidParamValueError("The given IBAN is not valid.");
        }

        BankAccount bankAccount = auth.getAuthorizedBankAccount(iBAN, pinCard, pinCode);

        // Check balance
        if (amount <= 0d) {
            throw new InvalidParamValueError("Amount should be greater than 0.");
        }

        Account account = iBAN.endsWith("S")
                ? bankAccount.getSavingAccount() : bankAccount.getCheckingAccount();
        if (account == null) {
            throw new InvalidParamValueError("The account does not exist");
        }

        // Update balance
        account.addBalance(new BigDecimal(amount).setScale(2, BigDecimal.ROUND_HALF_UP));
        accountRepository.save(bankAccount);

        // Save transaction
        Transaction transaction = new Transaction(
                null, iBAN, bankAccount.getPrimaryHolder().getName(),
                timeService.getDate().getDate(), amount, "Deposit");
        transactionRepository.save(transaction);
    }

    @Override
    public void payFromAccount(String sourceIBAN, String targetIBAN, String pinCard, String pinCode, Double amount)
            throws InvalidParamValueError, InvalidPINError, CardBlockedError {
        if (!IBANUtil.isValidIBAN(sourceIBAN)) {
            throw new InvalidParamValueError("The given source IBAN is not valid.");
        }

        if (!IBANUtil.isValidIBAN(targetIBAN)) {
            throw new InvalidParamValueError("The given target IBAN is not valid.");
        }

        BankAccount sourceBankAccount = auth.getAuthorizedBankAccount(sourceIBAN, pinCard, pinCode);
        Account sourceAccount = sourceIBAN.endsWith("S") ?
                sourceBankAccount.getSavingAccount() : sourceBankAccount.getCheckingAccount();

        BankAccount destBankAccount = accountRepository.findOne((int) IBANUtil.getAccountNumber(targetIBAN));
        Account destAccount = null;
        String destName = "";

        if (destBankAccount != null) {
            destAccount = targetIBAN.endsWith("S") ?
                    destBankAccount.getSavingAccount() : destBankAccount.getCheckingAccount();
            destName = destBankAccount.getPrimaryHolder().getName() + " "
                    + destBankAccount.getPrimaryHolder().getSurname();
        }

        BigDecimal amt = new BigDecimal(amount).setScale(2, BigDecimal.ROUND_HALF_UP);

        // Check balance
        if (amount <= 0.0d) {
            throw new InvalidParamValueError("Amount should be greater than 0.");
        }

        if (!sourceAccount.canPayAmount(amt)) {
            throw new InvalidParamValueError("Not enough balance on account.");
        }

        // Update balance
        sourceAccount.addBalance(amt.multiply(new BigDecimal(-1.0)));
        accountRepository.save(sourceBankAccount);

        if (destBankAccount != null) {
            destAccount.addBalance(amt);
            accountRepository.save(destBankAccount);
        }

        // Save Transaction
        Transaction transaction = new Transaction(
                sourceIBAN, targetIBAN, destName,
                timeService.getDate().getDate(), amount, "Payment with debit card.");
        transactionRepository.save(transaction);
    }

    @Override
    public void forceTransactionAccount(Account account, BigDecimal amount, String description) {
        account.addBalance(amount.setScale(2, BigDecimal.ROUND_HALF_UP));
        accountRepository.save(account.getAccount());

        String iBAN = IBANUtil.generateIBAN(account.getAccount()) + (account instanceof CheckingAccount ? "S" : "");

        // Save Transaction
        Transaction transaction;
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            transaction = new Transaction(
                    iBAN, "", "Banq",
                    timeService.getDate().getDate(), amount.multiply(new BigDecimal("-1.0")).doubleValue(), description);
        } else {
            transaction = new Transaction(
                    "", iBAN,
                    account.getAccount().getPrimaryHolder().getName()
                            + " " + account.getAccount().getPrimaryHolder().getSurname(),
                    timeService.getDate().getDate(), amount.doubleValue(), description);
        }
        transactionRepository.save(transaction);
    }

    @Override
    public void transferMoney(String authToken, String sourceIBAN, String targetIBAN, String targetName,
                              Double amount, String description) throws InvalidParamValueError, NotAuthorizedError {
        if (!IBANUtil.isValidIBAN(sourceIBAN)) {
            throw new InvalidParamValueError("The given source IBAN is not valid.");
        }

        if (!IBANUtil.isValidIBAN(targetIBAN)) {
            throw new InvalidParamValueError("The given target IBAN is not valid.");
        }

        BankAccount sourceBankAccount = auth.getAuthorizedBankAccount(authToken, sourceIBAN);
        Account sourceAccount = sourceBankAccount.getCheckingAccount();

        BankAccount destBankAccount = accountRepository.findOne((int) IBANUtil.getAccountNumber(targetIBAN));
        Account destAccount = destBankAccount.getCheckingAccount();

        BigDecimal amt = new BigDecimal(amount).setScale(2, BigDecimal.ROUND_HALF_UP);

        // Check balance
        if (amount <= 0.0d) {
            throw new InvalidParamValueError("Amount should be greater than 0.");
        }

        if (!sourceBankAccount.getCheckingAccount().canPayAmount(amt)) {
            throw new InvalidParamValueError("Not enough balance on account.");
        }

        // Update balance
        sourceAccount.addBalance(amt.multiply(new BigDecimal(-1.0)));
        destAccount.addBalance(amt);
        accountRepository.save(sourceBankAccount);
        accountRepository.save(destBankAccount);

        // Save Transaction
        Transaction transaction = new Transaction(
                sourceIBAN, targetIBAN, targetName,
                timeService.getDate().getDate(), amount, description);
        transactionRepository.save(transaction);
    }

}
