package honours.ing.banq.error;

/**
 * @author jeffrey
 * @since 31-5-17
 */
public class NoEffectError extends Exception {

    public NoEffectError() {
        super();
    }

    public NoEffectError(String s) {
        super(s);
    }

    public NoEffectError(String s, Throwable throwable) {
        super(s, throwable);
    }

    public NoEffectError(Throwable throwable) {
        super(throwable);
    }

    protected NoEffectError(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }
}
