package honours.ing.banq.account.bean;

import honours.ing.banq.card.Card;
import honours.ing.banq.util.IBANUtil;

/**
 * This bean is the return type for some of the {@link honours.ing.banq.account.BankAccountService}'s methods.
 * @author Jeffrey Bakker
 * @since 29-5-17
 */
public class NewAccountBean {

    private String iBAN;
    private String pinCard;
    private String pinCode;

    /**
     * Creates a new {@link NewAccountBean} with the given parameters.
     * @param card the card
     */
    public NewAccountBean(Card card) {
        iBAN = IBANUtil.generateIBAN(card.getAccount());
        pinCard = card.getCardNumber();
        pinCode = card.getPin();
    }

    /**
     * Returns the iBAN of this card.
     * @return the iBAN
     */
    public String getiBAN() {
        return iBAN;
    }

    /**
     * Returns the card number of the card.
     * @return the card number
     */
    public String getPinCard() {
        return pinCard;
    }

    /**
     * Returns the pin code of the card.
     * @return the pin code
     */
    public String getPinCode() {
        return pinCode;
    }
}
