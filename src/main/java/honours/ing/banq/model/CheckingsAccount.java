package honours.ing.banq.model;

import honours.ing.banq.database.annotation.DBNotNull;

/**
 * Created by Kevin Witlox on 23-2-2017.
 */
public class CheckingsAccount extends Account {

    @DBNotNull
    private String iban;

    public CheckingsAccount(String name, User user, Money balance, String iban) {
        super(name, user, balance);
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

}
