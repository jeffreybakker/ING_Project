package honours.ing.banq.card;

import honours.ing.banq.BoilerplateTest;
import honours.ing.banq.access.bean.NewCardBean;
import honours.ing.banq.auth.CardBlockedError;
import honours.ing.banq.auth.InvalidPINError;
import honours.ing.banq.transaction.TransactionService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

/**
 * @author jeffrey
 * @since 3-8-17
 */
public class CardServiceTest extends BoilerplateTest {

    @Autowired
    private CardService service;

    @Autowired
    private TransactionService transactionService;

    @Test(expected = CardBlockedError.class)
    public void blockCard() throws Exception {
        for (int i = 0; i < 3; i++) {
            try {
                transactionService.depositIntoAccount(account1.iBan, account1.cardNumber, "0000", 10.0);
            } catch (InvalidPINError ignored) { }
        }

        transactionService.depositIntoAccount(account1.iBan, account1.cardNumber, account1.pin, 10.0);
    }

    @Test
    public void unblockCard() throws Exception {
        try {
            blockCard();
        } catch (CardBlockedError ignored) { }

        service.unblockCard(account1.token, account1.iBan, account1.cardNumber);

        // Now test if the card is usable again
        transactionService.depositIntoAccount(account1.iBan, account1.cardNumber, account1.pin, 10.0);
    }

    @Test
    public void invalidateCard() throws Exception {
        NewCardBean res = service.invalidateCard(account1.token, account1.iBan, account1.cardNumber, false);
        account1.cardNumber = res.getPinCard();
        account1.pin = res.getPinCode();
    }

}