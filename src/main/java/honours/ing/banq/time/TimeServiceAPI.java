package honours.ing.banq.time;

import com.googlecode.jsonrpc4j.JsonRpcParam;
import com.googlecode.jsonrpc4j.JsonRpcService;
import honours.ing.banq.InvalidParamValueError;
import honours.ing.banq.access.NoEffectError;
import honours.ing.banq.time.bean.DateBean;

/**
 * @author Kevin Witlox
 * @since 13-7-2017.
 */
@JsonRpcService("/api/time")
public interface TimeServiceAPI {

    void simulateTime(@JsonRpcParam("nrOfDays") int nrOfDays) throws InvalidParamValueError;

    void reset() throws NoEffectError;

    DateBean getDate();

}
