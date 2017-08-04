package honours.ing.banq.transaction;

import com.googlecode.jsonrpc4j.spring.AutoJsonRpcServiceImpl;
import honours.ing.banq.InvalidParamValueError;
import honours.ing.banq.account.BankAccount;
import honours.ing.banq.account.BankAccountRepository;
import honours.ing.banq.auth.AuthService;
import honours.ing.banq.auth.CardBlockedError;
import honours.ing.banq.auth.InvalidPINError;
import honours.ing.banq.auth.NotAuthorizedError;
import honours.ing.banq.card.CardRepository;
import honours.ing.banq.customer.Customer;
import honours.ing.banq.util.IBANUtil;
import honours.ing.banq.time.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Kevin Witlox
 * @since 4-6-2017
 */
@Service
@AutoJsonRpcServiceImpl
@Transactional
public class TransactionServiceImpl implements TransactionService {

    // TODO: Sanitize user input

    // Services
    @Autowired
    private AuthService auth;

    // Repositories
    @Autowired
    private BankAccountRepository bankAccountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private CardRepository cardRepository;

    @Override
    public void depositIntoAccount(String iBAN, String pinCard, String pinCode, Double amount)
            throws InvalidParamValueError, InvalidPINError, CardBlockedError {
        if (!IBANUtil.isValidIBAN(iBAN)) {
            throw new InvalidParamValueError("The given IBAN is not valid.");
        }

        BankAccount bankAccount = auth.getAuthorizedAccount(iBAN, pinCard, pinCode);

        // Check balance
        if (amount <= 0d) {
            throw new InvalidParamValueError("Amount should be greater than 0.");
        }

        // Update balance
        bankAccount.addBalance(amount);
        bankAccountRepository.save(bankAccount);

        // Save transaction
        Transaction transaction = new Transaction(
                null, iBAN, bankAccount.getPrimaryHolder().getName(),
                TimeUtil.getDate(), amount, "Deposit");
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

        BankAccount fromBankAccount = auth.getAuthorizedAccount(sourceIBAN, pinCard, pinCode);
        BankAccount toBankAccount = bankAccountRepository.findOne((int) IBANUtil.getAccountNumber(targetIBAN));

        // Check balance
        if (fromBankAccount.getBalance() - amount < 0) {
            throw new InvalidParamValueError("Not enough balance on account.");
        }

        if (amount <= 0) {
            throw new InvalidParamValueError("Amount should be greater than 0.");
        }

        // Update balance
        fromBankAccount.subBalance(amount);
        toBankAccount.addBalance(amount);
        bankAccountRepository.save(fromBankAccount);
        bankAccountRepository.save(toBankAccount);

        // Save Transaction
        Transaction transaction = new Transaction(
                sourceIBAN, targetIBAN, toBankAccount.getPrimaryHolder().getName(),
                TimeUtil.getDate(), amount, "Payment with debit card.");
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

        BankAccount fromBankAccount = bankAccountRepository.findOne((int) IBANUtil.getAccountNumber(sourceIBAN));
        BankAccount toBankAccount = bankAccountRepository.findOne((int) IBANUtil.getAccountNumber(targetIBAN));
        Customer customer = auth.getAuthorizedCustomer(authToken);

        // Check if bank account is held by customer
        if (!fromBankAccount.getHolders().contains(customer) && !fromBankAccount.getPrimaryHolder().equals(customer)) {
            throw new NotAuthorizedError();
        }

        // Check balance
        if (fromBankAccount.getBalance() - amount < 0) {
            throw new InvalidParamValueError("Not enough balance on account.");
        }

        if (amount <= 0) {
            throw new InvalidParamValueError("Amount should be greater than 0.");
        }

        // Update balance
        fromBankAccount.subBalance(amount);
        toBankAccount.addBalance(amount);
        bankAccountRepository.save(fromBankAccount);
        bankAccountRepository.save(toBankAccount);

        // Save Transaction
        Transaction transaction = new Transaction(
                sourceIBAN, targetIBAN, targetName,
                TimeUtil.getDate(), amount, description);
        transactionRepository.save(transaction);
    }

}
