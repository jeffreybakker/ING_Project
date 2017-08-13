package honours.ing.banq.variables;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author jeffrey
 * @since 5-8-17
 */
@Service
@Transactional(readOnly = true)
public class VarService {

    @Autowired
    private VarRepository repository;

    public String getVariable(String key) {
        return getVariable(key, "");
    }

    public String getVariable(String key, String expectedValue) {
        Variable var = repository.findOne(key);

        if (var == null) {
            return expectedValue;
        }

        return var.getValue();
    }

    public void setVariable(String key, String value) {
        Variable var = repository.findOne(key);
        if (var == null) {
            var = new Variable(key, value);
        } else {
            var.setValue(value);
        }

        repository.save(var);
    }

}
