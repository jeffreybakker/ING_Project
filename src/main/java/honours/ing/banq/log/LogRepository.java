package honours.ing.banq.log;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author jeffrey
 * @since 6-8-17
 */
public interface LogRepository extends JpaRepository<Log, Long> {

    List<Log> findLogsByTimeStampBetween(long a, long b);

}
