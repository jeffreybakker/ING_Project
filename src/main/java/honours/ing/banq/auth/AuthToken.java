package honours.ing.banq.auth;

/**
 * @author Kevin Witlox
 * @since 3-7-2017.
 */
public class AuthToken {

    private String authToken;

    public AuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getAuthToken() {
        return authToken;
    }
}
