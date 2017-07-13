package honours.ing.banq.time;

import com.googlecode.jsonrpc4j.spring.AutoJsonRpcServiceImpl;
import honours.ing.banq.access.NoEffectError;
import honours.ing.banq.time.bean.DateBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Kevin Witlox
 * @since 13-7-2017.
 */
@Component
@AutoJsonRpcServiceImpl
public class TimeServiceAPIImpl implements TimeServiceAPI {

    @Autowired
    private TimeService timeService;

    @Override
    public void simulateTime(int nrOfDays) {
        timeService.simulateTime(nrOfDays);
    }

    @Override
    public void reset() throws NoEffectError {
        timeService.reset();
    }

    @Override
    public DateBean getDate() {
        return timeService.getDate();
    }

}
