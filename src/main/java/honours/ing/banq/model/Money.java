package honours.ing.banq.model;

import java.util.Currency;

/**
 * A class representation of an amount of money in a given currency.
 * @author Kevin Witlox & Jeffrey Bakker
 * @since 23-2-2017
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class Money {

    private Currency currency;

    private short minor;
    private long major;

    /**
     * Creates a copy of another money object.
     * @param cp the object to be copied
     */
    public Money(Money cp) {
        this.currency = cp.currency;
        this.minor = cp.minor;
        this.major = cp.major;
    }

    /**
     * Creates a new <code>Money</code> object with the given currency and initializes the amount to <i>0.0</i>.
     * @param currency the currency for this object
     */
    public Money(Currency currency) {
        this.currency = currency;

        minor = 0;
        major = 0;
    }

    /**
     * Creates a new <code>Money</code> object with the given currency and the given major and minor values of the
     * amount of money.
     * @param currency the currency
     * @param major the major amount (like whole euros)
     * @param minor the minor amount (like euro-cents)
     */
    public Money(Currency currency, long major, short minor) {
        this.currency = currency;
        this.major = major;
        this.minor = minor;
    }

    /**
     * Deposits an amount of money to this object.
     * @param amount the amount of money
     */
    public void deposit(Money amount) {
        if (amount.currency != currency) {
            throw new IllegalArgumentException(
                    "You can only do money operations on two Money objects with the same currency");
        } else if (amount.major < 0 || amount.minor < 0) {
            throw new IllegalArgumentException("You may not deposit a negative amount of money");
        }

        major += amount.major;
        minor += amount.minor;

        if (major >= 0) {
            major += minor / 100;
            minor %= 100;
        } else {
            while (minor < 0) {
                minor += 100;
                major--;
            }
        }
    }

    /**
     * Withdraws an amount of money from this object.
     * @param amount the amount of money
     */
    public void withdraw(Money amount) {
        if (amount.currency != currency) {
            throw new IllegalArgumentException(
                    "You can only do money operations on two Money objects with the same currency");
        } else if (amount.major < 0 || amount.minor < 0) {
            throw new IllegalArgumentException("You may not withdraw a negative amount of money");
        }

        major -= amount.major;
        minor -= amount.minor;

        if (major < 0) {
            major += minor / 100;
            minor %= 100;
        } else {
            while (minor > 0) {
                minor -= 100;
                major--;
            }
        }
    }

    /**
     * Returns whether it is possible or not to withdraw a certain amount of money from this object without going into
     * the negative values.
     * @param amount the amount to check
     * @return <code>true</code> if the resulting balance would be above 0.0
     */
    public boolean canWithdraw(Money amount) {
        if (amount.currency != currency) {
            throw new IllegalArgumentException(
                    "You can only do money operations on two Money objects with the same currency");
        }

        Money copy = new Money(this);
        copy.withdraw(amount);

        return copy.major >= 0 && copy.minor >= 0;
    }

    /**
     * Sets the amount of this money object.
     * @param major the major value of the amount
     * @param minor the minor value of the amount
     */
    public void setAmount(long major, short minor) {
        this.major = major;
        this.minor = minor;
    }

    /**
     * Returns the currency for this object.
     * @return the currency
     */
    public Currency getCurrency() {
        return currency;
    }

    /**
     * Returns the minor value of the amount of this money object.
     * @return the minor value of the amount
     */
    public short getMinor() {
        return minor;
    }

    /**
     * Sets the minor value of the amount of this money object.
     * @param minor the minor value of the amount
     */
    public void setMinor(short minor) {
        this.minor = minor;
    }

    /**
     * Returns the major value of the amount of this money object.
     * @return the major value of the amount
     */
    public long getMajor() {
        return major;
    }

    /**
     * Sets the major value of the amount of this money object.
     * @param major the major value of the amount
     */
    public void setMajor(long major) {
        this.major = major;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Money money = (Money) o;

        if (minor != money.minor) return false;
        if (major != money.major) return false;
        return currency != null ? currency.equals(money.currency) : money.currency == null;

    }

    @Override
    public int hashCode() {
        int result = currency != null ? currency.hashCode() : 0;
        result = 31 * result + (int) minor;
        result = 31 * result + (int) (major ^ (major >>> 32));
        return result;
    }

}
