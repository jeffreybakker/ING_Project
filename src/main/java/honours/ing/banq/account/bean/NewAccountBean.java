package honours.ing.banq.account.bean;

import honours.ing.banq.account.BankAccount;
import honours.ing.banq.card.Card;
import honours.ing.banq.util.IBANUtil;

/**
 * Created by jeffrey on 29-5-17.
 */
public class NewAccountBean {

    private String iBAN;
    private int pinCard;
    private int pinCode;

    public NewAccountBean(Card card) {
        iBAN = IBANUtil.generateIBAN(card.getAccount());
        pinCard = card.getCardNumber();
        pinCode = card.getPin();
    }

    public String getiBAN() {
        return iBAN;
    }

    public int getPinCard() {
        return pinCard;
    }

    public int getPinCode() {
        return pinCode;
    }
}
