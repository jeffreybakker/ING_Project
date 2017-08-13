package honours.ing.banq.customer;

import javax.persistence.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Represents a customer as a class and contains all the data that we store about a single customer.
 * @author Jeffrey Bakker
 */
@Entity
public class Customer {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;

    // Name
    private String name;
    private String surname;
    private String initials;

    // Personal details
    private String dob;

    @Column(unique = true)
    private String ssn;

    // Physical address
    private String address;
    private String telephoneNumber;

    // Online accessibility
    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private String username;
    private String password;

    /**
     * @deprecated empty constructor for spring
     */
    public Customer() { }

    public Customer(String name, String surname, String initials, String dob, String ssn, String address, String telephoneNumber, String email, String username, String password) {
        this.name = name;
        this.surname = surname;
        this.initials = initials;
        this.dob = dob;
        this.ssn = ssn;
        this.address = address;
        this.telephoneNumber = telephoneNumber;
        this.email = email;
        this.username = username;
        this.password = password;
    }

    /**
     * Returns the ID of this customer.
     * @return the ID
     */
    public Integer getId() {
        return id;
    }

    /**
     * Returns the first name of this customer.
     * @return the first name
     */
    public String getName() {
        return name + " " + surname;
    }

    /**
     * Returns the surname of this customer.
     * @return the surname
     */
    public String getSurname() {
        return surname;
    }

    /**
     * Returns the initials of this customer.
     * @return the initials
     */
    public String getInitials() {
        return initials;
    }

    /**
     * Returns the date of birth in dd-MM-yyy format.
     * @return the date of birth
     */
    public String getDob() {
        return dob;
    }

    /**
     * Returns the customer's social security number.
     * @return the social security number
     */
    public String getSsn() {
        return ssn;
    }

    /**
     * Returns this customer's address.
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * Returns this customer's phone number.
     * @return the phone number
     */
    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    /**
     * Returns this customer's email-address.
     * @return the email-address
     */
    public String getEmail() {
        return email;
    }

    /**
     * Returns this customer's username.
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Returns this customer's password.
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Returns true if the given string conforms the database date format, false otherwise.
     * @param date A string representing a date
     * @return True is success, false otherwise
     */
    public static boolean checkDate(String date) {
       try {
            DateFormat dateFormat = new SimpleDateFormat("dd:MM:yyyy");
            if (date.equals(dateFormat.format(dateFormat.parse(date)))) {
                return true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }

        return false;
    }

}
