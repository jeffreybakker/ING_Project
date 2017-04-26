package honours.ing.banq.customer;

import javax.persistence.*;
import java.util.Date;

/**
 * Represents a customer as a class and contains all the data that we store about a single customer.
 * @author Jeffrey Bakker
 */
@Entity
public class Customer {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;

    private String initials;
    private String name;
    private String surname;

    private Date birthDate;
    @Column(unique = true)
    private Integer socialSecurityNumber;

    private String address;
    private String telephoneNumber;
    @Column(unique = true)
    private String email;

    /**
     * Empty constructor for Spring.
     * @deprecated this constructors sole purpose is so that the class can be initialized by spring
     */
    public Customer() { }

    /**
     * Creates a new <code>Customer</code> with the given parameters.
     * @param initials the initials of the customer
     * @param name the first name of the customer
     * @param surname the surname of the customer
     * @param birthDate the date of birth of the customer
     * @param socialSecurityNumber the social security number of the customer
     * @param address the address of the customer
     * @param telephoneNumber the telephone number of the customer
     * @param email the email address of the customer
     */
    public Customer(String initials, String name, String surname, Date birthDate, Integer socialSecurityNumber, String address, String telephoneNumber, String email) {
        this.initials = initials;
        this.name = name;
        this.surname = surname;
        this.birthDate = birthDate;
        this.socialSecurityNumber = socialSecurityNumber;
        this.address = address;
        this.telephoneNumber = telephoneNumber;
        this.email = email;
    }

    /**
     * Returns the ID of the customer
     * @return the ID
     */
    public Integer getId() {
        return id;
    }

    /**
     * Returns the initials of the customer.
     * @return the initials
     */
    public String getInitials() {
        return initials;
    }

    /**
     * Sets the initials of the customer.
     * @param initials the initials
     */
    public void setInitials(String initials) {
        this.initials = initials;
    }

    /**
     * Returns the first name of the customer.
     * @return the first name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the customer.
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the surname of the customer.
     * @return the surname
     */
    public String getSurname() {
        return surname;
    }

    /**
     * Sets the surname of the customer.
     * @param surname the surname
     */
    public void setSurname(String surname) {
        this.surname = surname;
    }

    /**
     * Returns the date of birth of the customer.
     * @return the date of birth
     */
    public Date getBirthDate() {
        return birthDate;
    }

    /**
     * Sets the date of birth of the customer.
     * @param birthDate the date of birth
     */
    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    /**
     * Returns the social security number of the customer.
     * @return the social security number
     */
    public Integer getSocialSecurityNumber() {
        return socialSecurityNumber;
    }

    /**
     * Sets the social security number of the customer.
     * @param socialSecurityNumber the social security number
     */
    public void setSocialSecurityNumber(Integer socialSecurityNumber) {
        this.socialSecurityNumber = socialSecurityNumber;
    }

    /**
     * Returns the address of the customer.
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * Sets the address of the customer.
     * @param address the address
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Returns the telephone number of the customer.
     * @return the telephone number
     */
    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    /**
     * Sets the telephone number of the customer.
     * @param telephoneNumber the telephone number
     */
    public void setTelephoneNumber(String telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
    }

    /**
     * Returns the email address of the customer.
     * @return the email address
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email address of the customer.
     * @param email the email address
     */
    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Customer customer = (Customer) o;

        return id.equals(customer.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
