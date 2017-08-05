package honours.ing.banq.variables;

import com.googlecode.jsonrpc4j.spring.AutoJsonRpcServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author jeffrey
 * @since 5-8-17
 */
@Service
@AutoJsonRpcServiceImpl
@Transactional(readOnly = true)
public class VarServiceImpl implements VarService {

    @Autowired
    private VarRepository repository;

    @Override
    public String getVariable(String key) {
        return getVariable(key, "");
    }

    @Override
    public String getVariable(String key, String expectedValue) {
        Variable var = repository.findOne(key);

        if (var == null) {
            return expectedValue;
        }

        return var.getValue();
    }

    @Override
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
