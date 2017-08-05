package honours.ing.banq.variables;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * This repository interacts with the database for the {@link Variable} entity.
 * @author Jeffrey Bakker
 * @since 5-8-17
 */
public interface VarRepository extends JpaRepository<Variable, String> {

}
