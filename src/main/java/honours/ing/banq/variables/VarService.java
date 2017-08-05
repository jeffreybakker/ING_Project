package honours.ing.banq.variables;

import com.googlecode.jsonrpc4j.JsonRpcService;

/**
 * @author Jeffrey Bakker
 * @since 5-8-17
 */
@JsonRpcService("/api/var")
public interface VarService {

    String getVariable(String key);
    String getVariable(String key, String expectedValue);

    void setVariable(String key, String value);

}
