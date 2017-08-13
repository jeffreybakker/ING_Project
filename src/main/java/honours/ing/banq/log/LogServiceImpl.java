package honours.ing.banq.log;

import com.googlecode.jsonrpc4j.spring.AutoJsonRpcServiceImpl;
import honours.ing.banq.Application;
import honours.ing.banq.error.InvalidParamValueError;
import honours.ing.banq.log.bean.LogBean;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * An implementation for the {@link LogService}.
 * @author Jeffrey Bakker
 * @since 6-8-17
 */
@Service
@AutoJsonRpcServiceImpl
@Transactional(readOnly = true)
public class LogServiceImpl implements LogService {

    private static final DateFormat INPUT_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    private Logger logger = Application.getLogger();

    // Repositories
    private LogRepository repository;

    @Autowired
    public LogServiceImpl(LogRepository repository) {
        this.repository = repository;
    }

    @LogIgnore
    @Transactional
    @Override
    public void log(String message) {
        logger.info(message);

        Log log = new Log(message);
        repository.save(log);
    }

    @Override
    public List<LogBean> getEventLogs(String beginDate, String endDate) {
        long a, b;
        try {
            a = INPUT_DATE_FORMAT.parse(beginDate).getTime();
            b = INPUT_DATE_FORMAT.parse(endDate).getTime();
        } catch (ParseException e) {
            throw new InvalidParamValueError("Either the beginDate or the endDate was in an invalid format");
        }

        return LogBean.generate(repository.findLogsByTimeStampBetween(a, b));
    }

}
