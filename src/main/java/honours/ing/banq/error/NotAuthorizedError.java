package honours.ing.banq.error;

/**
 * @author jeffrey
 * @since 14-5-17
 */
public class NotAuthorizedError extends Exception {

    private static final String MESSAGE = "The authenticated user is not authorized to perform this action";

    public NotAuthorizedError() {
        super(MESSAGE);
    }

    public NotAuthorizedError(Throwable throwable) {
        super(MESSAGE, throwable);
    }

    protected NotAuthorizedError(Throwable throwable, boolean b, boolean b1) {
        super(MESSAGE, throwable, b, b1);
    }
}
