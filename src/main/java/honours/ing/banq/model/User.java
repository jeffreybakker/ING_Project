package honours.ing.banq.model;

import honours.ing.banq.database.annotation.DBNotNull;

import java.sql.ResultSet;

/**
 * @author Jeffrey Bakker
 */
public class User extends Model {

    @DBNotNull
    private String initials;

    @DBNotNull
    private String name;

    @DBNotNull
    private String lastName;

    public User(String name, String initials, String lastName) {
        this.initials = initials;
        this.lastName = lastName;
        this.name = name;

        id = genID();
    }

    /**
     * Parses the user object from a database ResultSet.
     * @param raw the raw data from the database
     * @return the parsed result
     */
    public static User parseRaw(ResultSet raw) {
        // TODO: Implement
        return null;
    }

    public String getInitials() {
        return initials;
    }

    public void setInitials(String initials) {
        this.initials = initials;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
