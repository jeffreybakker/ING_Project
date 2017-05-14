package honours.ing.banq.auth;

/**
 * @author jeffrey
 * @since 14-5-17
 */
public class AuthenticationError extends Exception {

    public AuthenticationError() {
        super("The user could not be authenticated. Invalid username, password or combination.");
    }
}
