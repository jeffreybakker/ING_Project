package honours.ing.banq.log;

import com.googlecode.jsonrpc4j.JsonRpcParam;
import com.googlecode.jsonrpc4j.JsonRpcService;
import honours.ing.banq.log.bean.LogBean;

import java.util.List;

/**
 * This service provides access to the log messages that have been made by the application.
 * @author Jeffrey Bakker
 * @since 6-8-17
 */
@JsonRpcService("/api/log")
public interface LogService {

    /**
     * Logs a message.
     * @param message the message to be logged
     */
    void log(String message);

    /**
     * Returns all event logs from a given time period.
     * @param beginDate the begin of time period
     * @param endDate   the end of the time period
     * @return a list of all event logs made in the given time period
     */
    List<LogBean> getEventLogs(@JsonRpcParam("beginDate") String beginDate, @JsonRpcParam("endDate") String endDate);

}
