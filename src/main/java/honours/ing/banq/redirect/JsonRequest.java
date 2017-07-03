package honours.ing.banq.redirect;

import java.util.Map;

/**
 * @author Kevin Witlox
 * @since 23-6-2017.
 */
public class JsonRequest {

    private String method;
    private Map<String, String> params;

    public JsonRequest(String method, Map<String, String> params) {
        this.method = method;
        this.params = params;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }
}
