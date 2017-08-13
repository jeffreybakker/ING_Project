package honours.ing.banq.auth.bean;

/**
 * A bean that is returned for some Service's methods.
 * @author Kevin Witlox
 * @since 3-7-2017.
 */
public class AuthTokenBean {

    private String authToken;

    /**
     * Creates a new {@link AuthTokenBean} with the given parameters.
     * @param authToken the authentication token
     */
    public AuthTokenBean(String authToken) {
        this.authToken = authToken;
    }

    /**
     * Returns the authentication token.
     * @return the authentication token
     */
    public String getAuthToken() {
        return authToken;
    }

    /**
     * Sets the authentication token.
     * @param authToken the authentication token
     */
    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
}
