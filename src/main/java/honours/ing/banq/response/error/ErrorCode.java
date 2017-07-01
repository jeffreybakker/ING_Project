package honours.ing.banq.response.error;

/**
 * @author Kevin Witlox
 * @since 1-7-2017.
 */
public enum ErrorCode {

    INVALIDPARAMVALUE(418),
    NOTAUTHORIZED(419),
    NOEFFECT(420),
    INVALIDPIN(421),
    AUTHENTICATION(422);

    private final int code;

    private ErrorCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

}
