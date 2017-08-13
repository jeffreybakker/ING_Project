package honours.ing.banq.error;

/**
 * @author jeffrey
 * @since 14-5-17
 */
public class InvalidParamValueError extends RuntimeException {

    public InvalidParamValueError() {
        super();
    }

    public InvalidParamValueError(String s) {
        super(s);
    }

    public InvalidParamValueError(String s, Throwable throwable) {
        super(s, throwable);
    }

    public InvalidParamValueError(Throwable throwable) {
        super(throwable);
    }

    protected InvalidParamValueError(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }
}
