package honours.ing.banq.error;

/**
 * @author jeffrey
 * @since 31-5-17
 */
public class InvalidPINError extends Exception {

    public InvalidPINError() {
        super();
    }

    public InvalidPINError(String s) {
        super(s);
    }

    public InvalidPINError(String s, Throwable throwable) {
        super(s, throwable);
    }

    public InvalidPINError(Throwable throwable) {
        super(throwable);
    }

    protected InvalidPINError(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }
}
