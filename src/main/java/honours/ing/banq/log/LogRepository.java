package honours.ing.banq.log;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * The repository for the {@link Log} entity.
 * @author Jeffrey Bakker
 * @since 6-8-17
 */
public interface LogRepository extends JpaRepository<Log, Long> {

    /**
     * Returns all logs between a and b.
     * @param a point a in time
     * @param b point b in time
     * @return a list of {@link Log}s.
     */
    List<Log> findLogsByTimeStampBetween(long a, long b);

}
