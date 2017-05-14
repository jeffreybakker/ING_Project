package honours.ing.banq.auth;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author jeffrey
 * @since 14-5-17
 */
public interface AuthRepository extends JpaRepository<Authentication, Long> {

    Authentication findByToken(String token);

}
