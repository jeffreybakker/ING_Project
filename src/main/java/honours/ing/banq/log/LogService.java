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

    enum Priority {

        DEBUG       (0),
        VERBOSE     (1),
        INFO        (2),
        WARNING     (3),
        ERROR       (4),
        WTF         (5);

        private int priority;

        Priority(int priority) {
            this.priority = priority;
        }

        public int getPriority() {
            return priority;
        }
    }

    default void d(String message) {
        log(Priority.DEBUG, message);
    }

    default void v(String message) {
        log(Priority.VERBOSE, message);
    }

    default void i(String message) {
        log(Priority.INFO, message);
    }

    default void w(String message) {
        log(Priority.WARNING, message);
    }

    default void e(String message) {
        log(Priority.ERROR, message);
    }

    default void wtf(String message) {
        log(Priority.WTF, message);
    }

    void log(Priority priority, String message);

    /**
     * Returns all event logs from a given time period.
     * @param beginDate the begin of time period
     * @param endDate   the end of the time period
     * @return a list of all event logs made in the given time period
     */
    List<LogBean> getEventLogs(@JsonRpcParam("beginDate") String beginDate, @JsonRpcParam("endDate") String endDate);

}
