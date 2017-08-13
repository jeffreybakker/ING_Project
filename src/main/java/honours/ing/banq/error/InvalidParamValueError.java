package honours.ing.banq.error;

/**
 * @author jeffrey
 * @since 14-5-17
 */
public class InvalidParamValueError extends RuntimeException {

    public InvalidParamValueError(String msg) {
        super(msg);
    }

}
