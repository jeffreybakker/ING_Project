package honours.ing.banq.info.bean;

import honours.ing.banq.account.BankAccount;
import honours.ing.banq.customer.Customer;

/**
 * Used to represent a user that has access to a certain {@link BankAccount}. Stores the username of
 * said user.
 *
 * @author Kevin Witlox
 */
public class BankAccountAccessBean {

    private String username;

    public BankAccountAccessBean(Customer customer) {
        this.username = customer.getUsername();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
