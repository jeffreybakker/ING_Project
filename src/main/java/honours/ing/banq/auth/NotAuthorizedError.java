package honours.ing.banq.auth;

/**
 * @author jeffrey
 * @since 14-5-17
 */
public class NotAuthorizedError extends Exception {

    public NotAuthorizedError() {
        super("The authenticated user is not authorized to perform this action");
    }
}
