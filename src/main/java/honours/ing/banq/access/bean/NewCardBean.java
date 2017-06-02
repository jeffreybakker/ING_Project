package honours.ing.banq.access.bean;

import honours.ing.banq.card.Card;

/**
 * @author jeffrey
 * @since 31-5-17
 */
public class NewCardBean {

    private int pinCard;
    private int pinCode;

    public NewCardBean(Card card) {
        pinCard = card.getCardNumber();
        pinCode = card.getPin();
    }

    public int getPinCard() {
        return pinCard;
    }

    public int getPinCode() {
        return pinCode;
    }
}
