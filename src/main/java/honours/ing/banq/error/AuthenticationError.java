package honours.ing.banq.error;

/**
 * @author jeffrey
 * @since 14-5-17
 */
public class AuthenticationError extends Exception {

    private static final String MESSAGE =
            "The user could not be authenticated. Invalid username, password or combination.";

    public AuthenticationError() {
        super(MESSAGE);
    }

    public AuthenticationError(Throwable throwable) {
        super(MESSAGE, throwable);
    }

    protected AuthenticationError(Throwable throwable, boolean b, boolean b1) {
        super(MESSAGE, throwable, b, b1);
    }

}
