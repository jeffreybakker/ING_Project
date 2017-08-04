package honours.ing.banq.access.bean;

import honours.ing.banq.card.Card;

/**
 * The return object for some of the {@link honours.ing.banq.access.AccessService}'s methods.
 * @author Jeffrey Bakker
 * @since 31-5-17
 */
public class NewCardBean {

    private String pinCard;
    private String pinCode;

    /**
     * Creates a new {@link NewCardBean}.
     * @param card the card for which to create this bean
     */
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
