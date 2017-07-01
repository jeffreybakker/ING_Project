package honours.ing.banq.response.error;

import java.util.Date;

/**
 * @author Kevin Witlox
 * @since 1-7-2017.
 */
public class ErrorBean {

    private int code;
    private String message;
    private String date;

    public ErrorBean(ErrorCode code, String message) {
        this.code = code.getCode();
        this.message = message;
        this.date = new Date().toString();
    }

}
