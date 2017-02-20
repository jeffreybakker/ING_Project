package honours.ing.banq.model;

import java.sql.ResultSet;

/**
 * @author Jeffrey Bakker
 */
public class Person {

    private int id;

    private String firstName;
    private String lastName;

    public Person(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;

        id = genID();
    }

    /**
     * Inserts this person in the database.
     */
    private void insert() {

    }

    /**
     * Updates this person in the database.
     */
    public void save() {
        // TODO: Implement
    }

    public static int genID() {
        // TODO: Implement
        return -1;
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

    public int getID() {
        return id;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
