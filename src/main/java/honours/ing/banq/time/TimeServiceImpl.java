package honours.ing.banq.time;

import com.googlecode.jsonrpc4j.spring.AutoJsonRpcServiceImpl;
import honours.ing.banq.InvalidParamValueError;
import honours.ing.banq.event.EventInterceptor;
import honours.ing.banq.time.bean.DateBean;
import honours.ing.banq.variables.VarService;
import org.hibernate.Session;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.persister.entity.AbstractEntityPersister;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * An implementation of the {@link TimeService}.
 * @author Jeffrey Bakker
 * @since 4-8-17
 */
@Service
@AutoJsonRpcServiceImpl
@Transactional(readOnly = true)
public class TimeServiceImpl implements TimeService {

    private static final long DAY = 1000 * 60 * 60 * 24; // second * minute * hour * day

    private static long addedMillis;
    private static TimeService instance;

    private EntityManager entityManager;
    private EventInterceptor eventInterceptor;

    private VarService vars;

    @Autowired
    public TimeServiceImpl(EntityManager em, VarService vars, EventInterceptor eventInterceptor) {
        instance = this;
        entityManager = em;
        this.eventInterceptor = eventInterceptor;
        this.vars = vars;

        addedMillis = Long.parseLong(vars.getVariable("time", "0"));
    }

    @Override
    public Object simulateTime(int nrOfDays) throws InvalidParamValueError {
        if (nrOfDays < 0) {
            throw new InvalidParamValueError("The amount of days may not be less than 0");
        }

        addedMillis += nrOfDays * DAY;
        save();

        return new Object();
    }

    @Transactional
    @Override
    public Object reset() {
        addedMillis = 0L;

        // Truncate all tables in the database
        List<String> tableNames = new ArrayList<>();
        Session session = entityManager.unwrap(Session.class);
        Map<String, ClassMetadata> hibernateMetadata = session.getSessionFactory().getAllClassMetadata();

        for (ClassMetadata data : hibernateMetadata.values()) {
            AbstractEntityPersister aep = (AbstractEntityPersister) data;
            tableNames.add(aep.getTableName());
        }

        entityManager.flush();
        entityManager.createNativeQuery("SET FOREIGN_KEY_CHECKS=0;").executeUpdate();

        tableNames.forEach((name) -> {
            // This is to test for some weird glitch where there is also a table name containing a select query for some
            // table, last time I checked it was for the "checking_account" table
            if (!name.startsWith("(")) {
                entityManager.createNativeQuery("TRUNCATE TABLE " + name).executeUpdate();
            }
        });

        entityManager.createNativeQuery("SET FOREIGN_KEY_CHECKS=1;").executeUpdate();

        eventInterceptor.calcTimers();

        return new Object();
    }

    @Override
    public void save() {
        vars.setVariable("time", "" + addedMillis);
    }

    @Override
    public DateBean getDate() {
        return new DateBean(getCurDate());
    }

    public static long currentTimeMillis() {
        return System.currentTimeMillis() + addedMillis;
    }

    public static Date getCurDate() {
        return new Date(currentTimeMillis());
    }

}
