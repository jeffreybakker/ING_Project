package honours.ing.banq.time;

import com.googlecode.jsonrpc4j.spring.AutoJsonRpcServiceImpl;
import honours.ing.banq.InvalidParamValueError;
import honours.ing.banq.time.bean.DateBean;
import org.hibernate.Session;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.persister.entity.AbstractEntityPersister;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.ArrayList;
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

    @Autowired
    private EntityManager entityManager;

    @Override
    public Object simulateTime(int nrOfDays) throws InvalidParamValueError {
        if (nrOfDays < 0) {
            throw new InvalidParamValueError("The amount of days may not be less than 0");
        }

        TimeUtil.addDays(nrOfDays);

        return new Object();
    }

    @Transactional
    @Override
    public Object reset() {
        TimeUtil.resetTime();

        // Truncate all tables in the database
        List<String> tableNames = new ArrayList<>();
        Session session = entityManager.unwrap(Session.class);
        Map<String, ClassMetadata> hibernateMetadata = session.getSessionFactory().getAllClassMetadata();

        for (ClassMetadata data : hibernateMetadata.values()) {
            AbstractEntityPersister aep = (AbstractEntityPersister) data;
            tableNames.add(aep.getTableName());
        }

        entityManager.flush();
        entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY FALSE").executeUpdate();
        tableNames.forEach((name) -> entityManager.createNativeQuery("TRUNCATE TABLE " + name).executeUpdate());
        entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY TRUE").executeUpdate();

        return new Object();
    }

    @Override
    public DateBean getDate() {
        return new DateBean(TimeUtil.getDate());
    }

}
