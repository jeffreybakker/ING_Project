package honours.ing.banq.card;

import honours.ing.banq.BoilerplateTest;
import honours.ing.banq.InvalidParamValueError;
import honours.ing.banq.access.bean.NewCardBean;
import honours.ing.banq.auth.NotAuthorizedError;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.*;

/**
 * @author Kevin Witlox
 * @since 12-7-2017.
 */
public class CardServiceTest extends BoilerplateTest {

    // Repositories
    @Autowired
    private CardRepository cardRepository;

    @Test
    public void invalidateCard() throws Exception {
        NewCardBean newCardBean;

        // NewPin false
        transactionService.depositIntoAccount(account1.iBan, account1.cardNumber, account1.pin, 15d);
        newCardBean = cardService.invalidateCard(account1.token, account1.iBan, account1.cardNumber, false);
        assertThat(infoService.getBalance(account1.token, account1.iBan).getBalance(), equalTo(7.5d));
        assertThat(cardRepository.findByCardNumber(account1.cardNumber).isInvalidated(), equalTo(true));
        assertThat(cardRepository.findByCardNumber(newCardBean.getPinCard()).isInvalidated(), equalTo(false));
        assertThat(cardRepository.findByCardNumber(newCardBean.getPinCard()).getPin(), equalTo(account1.pin));

        account1.cardNumber = newCardBean.getPinCard();

        // NewPin true
        newCardBean = cardService.invalidateCard(account1.token, account1.iBan, account1.cardNumber, true);
        assertThat(infoService.getBalance(account1.token, account1.iBan).getBalance(), equalTo(0d));
        assertThat(cardRepository.findByCardNumber(account1.cardNumber).isInvalidated(), equalTo(true));
        assertThat(cardRepository.findByCardNumber(newCardBean.getPinCard()).isInvalidated(), equalTo(false));
        assertThat(cardRepository.findByCardNumber(newCardBean.getPinCard()).getPin(), equalTo(newCardBean.getPinCode()));

    }

    @Test(expected = InvalidParamValueError.class)
    public void invalidateCardInvalidIBAN() throws Exception {
        cardService.invalidateCard(account1.token, "null", account1.cardNumber, false);
    }

    @Test(expected = InvalidParamValueError.class)
    public void invalidateCardInvalidCardNumber() throws Exception {
        cardService.invalidateCard(account1.token, account1.iBan, "null", false);
    }

    @Test(expected = NotAuthorizedError.class)
    public void invalidateCardInvalidToken() throws Exception {
        cardService.invalidateCard("null", account1.iBan, account1.cardNumber, false);
    }

    @Test(expected = NotAuthorizedError.class)
    public void invalidateCardWrongToken() throws Exception {
        cardService.invalidateCard(account1.token, account2.iBan, account2.cardNumber, false);
    }

    @Test(expected = NotAuthorizedError.class)
    public void invalidateCardWrongCardNumber() throws Exception {
        cardService.invalidateCard(account1.token, account1.iBan, account2.cardNumber, false);
    }

    @Test(expected = NotAuthorizedError.class)
    public void invalidateCardWrongIBAN() throws Exception {
        cardService.invalidateCard(account1.token, account2.iBan, account1.cardNumber, false);
    }

    @Test(expected = InvalidParamValueError.class)
    public void invalidateCardAlreadyInvalidated() throws Exception {
        cardService.invalidateCard(account1.token, account1.iBan, account1.cardNumber, false);
        cardService.invalidateCard(account1.token, account1.iBan, account1.cardNumber, false);
    }

}