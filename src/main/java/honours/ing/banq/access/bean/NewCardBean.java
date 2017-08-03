package honours.ing.banq.access.bean;

import honours.ing.banq.card.Card;

/**
 * @author jeffrey
 * @since 31-5-17
 */
public class NewCardBean {

    private String pinCard;
    private String pinCode;

    public NewCardBean(Card card) {
        pinCard = card.getCardNumber();
        pinCode = card.getPin();
    }

    public String getPinCard() {
        return pinCard;
    }

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCard(String pinCard) {
        this.pinCard = pinCard;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }
}
