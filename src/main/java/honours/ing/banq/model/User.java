package honours.ing.banq.model;

import honours.ing.banq.database.annotation.DBIgnored;
import honours.ing.banq.database.annotation.DBNotNull;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;

/**
 * @author Jeffrey Bakker
 */
public class User extends Model {

    @DBNotNull
    private String initials;

    @DBNotNull
    private String name;

    @DBNotNull
    private String surName;

    @DBNotNull
    private Date birthDate;

    @DBNotNull
    private int socialSecurityNumber;

    @DBNotNull
    private String address;

    @DBNotNull
    private String telephoneNumber;

    @DBNotNull
    private String email;

    public User(String name, String initials, String surName, Date birthDate, int socialSecurityNumber, String address,
                String telephoneNumber, String email) {
        this.initials = initials;
        this.surName = surName;
        this.name = name;

        this.birthDate = birthDate;
        this.socialSecurityNumber = socialSecurityNumber;
        this.address = address;
        this.telephoneNumber = telephoneNumber;
        this.email = email;

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

    public String getSurName() {
        return surName;
    }

    public void setSurName(String surName) {
        this.surName = surName;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public int getSocialSecurityNumber() {
        return socialSecurityNumber;
    }

    public void setSocialSecurityNumber(int socialSecurityNumber) {
        this.socialSecurityNumber = socialSecurityNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    public void setTelephoneNumber(String telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
