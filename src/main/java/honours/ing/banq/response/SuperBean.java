package honours.ing.banq.response;

import honours.ing.banq.response.error.ErrorBean;

/**
 * @author Kevin Witlox
 * @since 1-7-2017.
 */
public class SuperBean<E> {

    private final String jsonrpc = "2.0";
    public E result;
    private ErrorBean error = null;
    private String id;

    public SuperBean(E result, ErrorBean error, String id) {
        this.result = result;
        this.error = error;
        this.id = id;
    }

    public E getResult() {
        return result;
    }
}
