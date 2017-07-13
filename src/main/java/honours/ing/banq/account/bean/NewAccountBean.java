package honours.ing.banq.account.bean;

import honours.ing.banq.account.BankAccount;
import honours.ing.banq.card.Card;
import honours.ing.banq.util.IBANUtil;

import java.text.SimpleDateFormat;

/**
 * Created by jeffrey on 29-5-17.
 */
public class NewAccountBean {

    private String iBAN;
    private String pinCard;
    private String pinCode;
    private String expirationDate;

    public NewAccountBean(Card card) {
        iBAN = IBANUtil.generateIBAN(card.getAccount());
        pinCard = card.getCardNumber();
        pinCode = card.getPin();
        expirationDate = (new SimpleDateFormat("yyyy-MM-dd")).format(card.getExpirationDate());
    }

    public String getiBAN() {
        return iBAN;
    }

    public String getPinCard() {
        return pinCard;
    }

    public String getPinCode() {
        return pinCode;
    }
}
