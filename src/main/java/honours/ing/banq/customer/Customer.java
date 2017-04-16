package honours.ing.banq.customer;

import javax.persistence.*;
import java.util.Date;

/**
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
    private Integer socialSecurityNumber;

    private String address;
    private String telephoneNumber;
    private String email;

    public Customer() { }

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

    public Integer getId() {
        return id;
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

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public Integer getSocialSecurityNumber() {
        return socialSecurityNumber;
    }

    public void setSocialSecurityNumber(Integer socialSecurityNumber) {
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
