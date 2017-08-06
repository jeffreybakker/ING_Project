package honours.ing.banq.log.bean;

import honours.ing.banq.log.Log;
import honours.ing.banq.log.LogService;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * This is a bean that will be returned in some of the {@link LogService}'s methods
 * @author Jeffrey Bakker
 * @since 6-8-17
 */
public class LogBean {

    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

    private String timeStamp;
    private String eventLog;

    public LogBean(Log log) {
        this.timeStamp = DATE_FORMAT.format(new Date(log.getTimeStamp()));
        this.eventLog = log.getMessage();
    }

    public static List<LogBean> generate(List<Log> logs) {
        List<LogBean> res = new ArrayList<>();
        logs.forEach((l) -> res.add(new LogBean(l)));
        return res;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getEventLog() {
        return eventLog;
    }

    public void setEventLog(String eventLog) {
        this.eventLog = eventLog;
    }
}
