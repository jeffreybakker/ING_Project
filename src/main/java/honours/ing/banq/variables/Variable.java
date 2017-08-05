package honours.ing.banq.variables;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Represents a persistent variable that can be saved in the database.
 * @author Jeffrey Bakker
 * @since 5-8-17
 */
@Entity
class Variable {

    @Id
    private String varKey;

    private String value;

    @Deprecated
    Variable() { }

    /**
     * Creates a new variable with the given unique varKey and the given value.
     * @param key   the varKey of the variable
     * @param value the value of the variable
     */
    Variable(String key, String value) {
        this.varKey = key;
        this.value = value;
    }

    /**
     * Returns the varKey of the variable.
     * @return the varKey
     */
    String getVarKey() {
        return varKey;
    }

    /**
     * Returns the value of the variable.
     * @return the value
     */
    String getValue() {
        return value;
    }

    /**
     * Sets the value of the variable.
     * @param value the value
     */
    void setValue(String value) {
        this.value = value;
    }

}
