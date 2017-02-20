package honours.ing.banq.model;

import com.sun.istack.internal.NotNull;

import java.sql.ResultSet;

/**
 * @author Jeffrey Bakker
 */
public class Person extends Model {

    @NotNull
    private String firstName;
    @NotNull
    private String lastName;

    public Person(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;

        id = genID();
    }

    public static Person parseRaw(ResultSet raw) {
        // TODO: Implement
        return null;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
