package honours.ing.banq.auth;

/**
 * @author jeffrey
 * @since 3-8-17
 */
public class CardBlockedError extends Exception {

    public CardBlockedError() {
        super();
    }

    public CardBlockedError(String s) {
        super(s);
    }

    public CardBlockedError(String s, Throwable throwable) {
        super(s, throwable);
    }

    public CardBlockedError(Throwable throwable) {
        super(throwable);
    }

    protected CardBlockedError(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }
}
