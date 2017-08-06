package honours.ing.banq.log;

import honours.ing.banq.BoilerplateTest;
import honours.ing.banq.log.bean.LogBean;
import honours.ing.banq.time.TimeServiceImpl;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author jeffrey
 * @since 6-8-17
 */
public class LogServiceTest extends BoilerplateTest {

    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    @Autowired
    private LogService log;

    @Test
    public void getEventLogs() throws Exception {
        List<LogBean> logs = log.getEventLogs(
                DATE_FORMAT.format(new Date(1)), DATE_FORMAT.format(timeService.getDate().getDate()));

        assertTrue(logs.size() > 0);
    }

}