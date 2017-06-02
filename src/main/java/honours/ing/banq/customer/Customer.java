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

    // Name
    private String name;
    private String surname;
    private String initials;

    // Personal details
    private Date dob;

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

    public Customer(String name, String surname, String initials, Date dob, String ssn, String address, String telephoneNumber, String email, String username, String password) {
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

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getInitials() {
        return initials;
    }

    public Date getDob() {
        return dob;
    }

    public String getSsn() {
        return ssn;
    }

    public String getAddress() {
        return address;
    }

    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
