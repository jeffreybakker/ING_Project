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

    @Autowired
    private LogService log;

    @Test
    public void getEventLogs() throws Exception {
        List<LogBean> logs = log.getEventLogs(
                "1970-1-1", "2100-1-1");

        assertTrue(logs.size() > 0);
    }

}