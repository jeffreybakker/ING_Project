package honours.ing.banq.info.bean;

import honours.ing.banq.account.BankAccount;
import honours.ing.banq.customer.Customer;
import honours.ing.banq.util.IBANUtil;

/**
 * Used to represent an account a certain user has access to. Stores its iBAN and its owner (can be
 * different than the user).
 *
 * @author Kevin Witlox
 */
public class UserAccessBean {

    private String iBAN;
    private String owner;

    public UserAccessBean(BankAccount account, Customer customer) {
        this.iBAN = IBANUtil.generateIBAN(account);
        this.owner = customer.getName();
    }

    public String getiBAN() {
        return iBAN;
    }

    public void setiBAN(String iBAN) {
        this.iBAN = iBAN;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }
}
