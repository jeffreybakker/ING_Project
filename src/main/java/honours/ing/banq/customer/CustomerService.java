package honours.ing.banq.customer;

import com.googlecode.jsonrpc4j.JsonRpcParam;
import com.googlecode.jsonrpc4j.JsonRpcService;

import java.util.Date;
import java.util.List;

/**
 * @author jeffrey
 * @since 17-4-17
 */
@JsonRpcService("/api/customer")
public interface CustomerService {

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
    Customer createCustomer(
            @JsonRpcParam("initials") String initials, @JsonRpcParam("name") String name, @JsonRpcParam("surname") String surname,
            @JsonRpcParam("birthDate") Date birthDate, @JsonRpcParam("socialSecurityNumber") Integer socialSecurityNumber,
            @JsonRpcParam("address") String address, @JsonRpcParam("telephoneNumber") String telephoneNumber, @JsonRpcParam("email") String email);

    /**
     * Deletes the customer with the given id.
     * @param id the id of the customer
     */
    void deleteCustomer(@JsonRpcParam("id") Integer id);

    /**
     * Returns all customers with the given first name.
     * @param name the first name
     * @return a list of customers with the given first name
     */
    List<Customer> findByName(@JsonRpcParam("name") String name);

    /**
     * Returns all customers with the given surname.
     * @param surname the surname
     * @return a list of customers with the given surname
     */
    List<Customer> findBySurname(@JsonRpcParam("surname") String surname);

    /**
     * Returns a list of customers with the given combination of first name and surname.
     * @param name the first name
     * @param surname the surname
     * @return a list of customers that match the given combination of first name and surname
     */
    List<Customer> findByNameAndSurname(@JsonRpcParam("name") String name, @JsonRpcParam("surname") String surname);

    /**
     * Returns the customer with the given social security number.
     * @param socialSecurityNumber the social security number
     * @return the customer with the given social security number
     */
    Customer findBySocialSecurityNumber(@JsonRpcParam("socialSecurityNumber") Integer socialSecurityNumber);

    /**
     * Returns the customer with the given email address.
     * @param email the email address
     * @return the customer with the given email address
     */
    Customer findByEmail(@JsonRpcParam("email") String email);

    /**
     * Returns a list of all customers.
     * @return a list of all customers
     */
    List<Customer> listAllCustomers();

    /**
     * Updates a customer with the given id.
     * @param id the id of the customer
     * @param initials the initials of the customer
     * @param name the first name of the customer
     * @param surname the surname of the customer
     * @param birthDate the date of birth of the customer
     * @param socialSecurityNumber the social security number of the customer
     * @param address the address of the customer
     * @param telephoneNumber the telephone number of the customer
     * @param email the email address of the customer
     * @return the updated customer object
     */
    Customer updateCustomer(@JsonRpcParam("id") Integer id,
            @JsonRpcParam("initials") String initials, @JsonRpcParam("name") String name, @JsonRpcParam("surname") String surname,
            @JsonRpcParam("birthDate") Date birthDate, @JsonRpcParam("socialSecurityNumber") Integer socialSecurityNumber,
            @JsonRpcParam("address") String address, @JsonRpcParam("telephoneNumber") String telephoneNumber, @JsonRpcParam("email") String email);

    /**
     * Updates the name of the customer with the given id.
     * @param id the id of the customer
     * @param initials the new initials
     * @param name the new first name
     * @param surname the new surname
     * @return the updated customer
     */
    Customer updateName(@JsonRpcParam("id") Integer id, @JsonRpcParam("initials") String initials, @JsonRpcParam("name") String name, @JsonRpcParam("surname") String surname);

    /**
     * Updates the address of the customer with the given id.
     * @param id the id of the customer
     * @param address the new address of the customer
     * @return the updated customer
     */
    Customer updateAddress(@JsonRpcParam("id") Integer id, @JsonRpcParam("address") String address);

    /**
     * Updates the telephone number of the customer with the given id.
     * @param id the id of the customer
     * @param telephoneNumber the telephone number of the customer
     * @return the updated customer
     */
    Customer updateTelephoneNumber(@JsonRpcParam("id") Integer id, @JsonRpcParam("telephoneNumber") String telephoneNumber);

    /**
     * Updates the email address of the customer with the given id.
     * @param id the id of the customer
     * @param email the telephone number of the customer
     * @return the updated customer
     */
    Customer updateEmail(@JsonRpcParam("id") Integer id, @JsonRpcParam("email") String email);

}
