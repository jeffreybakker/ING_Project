package honours.ing.banq.access.bean;

import honours.ing.banq.card.Card;

import java.text.SimpleDateFormat;

/**
 * @author jeffrey
 * @since 31-5-17
 */
public class NewCardBean {

    private String pinCard;
    private String pinCode;
    private String expirationDate;

    public NewCardBean(String pinCard) {
        this.pinCard = pinCard;
    }

    public NewCardBean(Card card) {
        this.pinCard = card.getCardNumber();
        this.pinCode = card.getPin();
        this.expirationDate = (new SimpleDateFormat("yyyy-MM-dd")).format(card.getExpirationDate());
    }

    public String getPinCard() {
        return pinCard;
    }

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

}
