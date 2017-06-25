package honours.ing.banq.transaction;

import com.googlecode.jsonrpc4j.spring.AutoJsonRpcServiceImpl;
import honours.ing.banq.InvalidParamValueError;
import honours.ing.banq.account.BankAccount;
import honours.ing.banq.account.BankAccountRepository;
import honours.ing.banq.auth.AuthService;
import honours.ing.banq.auth.InvalidPINError;
import honours.ing.banq.auth.NotAuthorizedError;
import honours.ing.banq.card.Card;
import honours.ing.banq.card.CardRepository;
import honours.ing.banq.customer.Customer;
import honours.ing.banq.util.IBANUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.*;

/**
 * @author Kevin Witlox
 * @since 4-6-2017
 */
@Service
@AutoJsonRpcServiceImpl
@Transactional(readOnly = true)
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
            throws InvalidParamValueError, InvalidPINError {
        BankAccount bankAccount = bankAccountRepository.findOne((int) IBANUtil.getAccountNumber
                (iBAN));
        Card card = cardRepository.findByAccountAndCardNumber(bankAccount, pinCard);

        // Check for card matching iBAN
        if (card == null) {
            throw new InvalidParamValueError("The given card does not belong to the given iBAN.");
        }

        // Check pin code
        if (!Objects.equals(card.getPin(), pinCode)) {
            throw new InvalidPINError();
        }

        // Check balance
        if (bankAccount.getBalance() - amount < 0) {
            throw new InvalidParamValueError("Not enough balance on account.");
        }

        // Update balance
        bankAccount.addBalance(amount);
        bankAccountRepository.save(bankAccount);

        // Save transaction
        Transaction transaction = new Transaction(null, iBAN, bankAccount.getPrimaryHolder()
                .getName(), new Date(), amount, "Deposit");
        transactionRepository.save(transaction);
    }

    @Override
    public void payFromAccount(String sourceIBAN, String targetIBAN, String pinCard, String
            pinCode, Double amount) throws InvalidParamValueError, InvalidPINError {
        BankAccount fromBankAccount = bankAccountRepository.findOne((int) IBANUtil
                .getAccountNumber(sourceIBAN));
        BankAccount toBankAccount = bankAccountRepository.findOne((int) IBANUtil.getAccountNumber
                (targetIBAN));
        Card card = cardRepository.findByAccountAndCardNumber(fromBankAccount, pinCard);

        // Check for card matching iBAN
        if (card == null) {
            throw new InvalidParamValueError("The given card does not belong to the given iBAN.");
        }

        // Check pin code
        if (!Objects.equals(card.getPin(), pinCode)) {
            throw new InvalidPINError();
        }

        // Check balance
        if (fromBankAccount.getBalance() - amount < 0) {
            throw new InvalidParamValueError("Not enough balance on account.");
        }

        // Update balance
        fromBankAccount.subBalance(amount);
        toBankAccount.addBalance(amount);
        bankAccountRepository.save(fromBankAccount);
        bankAccountRepository.save(toBankAccount);

        // Save Transaction
        Transaction transaction = new Transaction(sourceIBAN, targetIBAN, toBankAccount
                .getPrimaryHolder().getName(), new Date(), amount, "Payment with debit card.");
        transactionRepository.save(transaction);
    }

    @Override
    public void transferMoney(String authToken, String sourceIBAN, String targetIBAN, String
            targetName, Double amount, String description) throws InvalidParamValueError,
            NotAuthorizedError {
        BankAccount fromBankAccount = bankAccountRepository.findOne((int) IBANUtil
                .getAccountNumber(sourceIBAN));
        BankAccount toBankAccount = bankAccountRepository.findOne((int) IBANUtil.getAccountNumber
                (targetIBAN));
        Customer customer = auth.getAuthorizedCustomer(authToken);

        // Check if bank account is held by customer
        if (!fromBankAccount.getHolders().contains(customer)) {
            throw new NotAuthorizedError();
        }

        // Check balance
        if (fromBankAccount.getBalance() - amount < 0) {
            throw new InvalidParamValueError("Not enough balance on account.");
        }

        // Update balance
        fromBankAccount.subBalance(amount);
        toBankAccount.addBalance(amount);
        bankAccountRepository.save(fromBankAccount);
        bankAccountRepository.save(toBankAccount);

        // Save Transaction
        Transaction transaction = new Transaction(sourceIBAN, targetIBAN, targetName, new Date(),
                amount, description);
        transactionRepository.save(transaction);
    }

}
