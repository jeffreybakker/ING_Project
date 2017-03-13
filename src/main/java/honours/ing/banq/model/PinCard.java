package honours.ing.banq.model;

import honours.ing.banq.database.annotation.DBNotNull;

/**
 * @author Jeffrey Bakker
 */
public class PinCard extends Model {

    @DBNotNull
    private User user;

    @DBNotNull
    private Account account;

    public PinCard(Account account, User user) {
        this.account = account;
        this.user = user;

        id = genID();
    }

    public User getUser() {
        return user;
    }

    public Account getAccount() {
        return account;
    }
}
