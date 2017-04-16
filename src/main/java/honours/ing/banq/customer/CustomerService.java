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

    Customer createCustomer(
            @JsonRpcParam("initials") String initials, @JsonRpcParam("name") String name, @JsonRpcParam("surname") String surname,
            @JsonRpcParam("birthDate") Date birthDate, @JsonRpcParam("socialSecurityNumber") Integer socialSecurityNumber,
            @JsonRpcParam("address") String address, @JsonRpcParam("telephoneNumber") String telephoneNumber, @JsonRpcParam("email") String email);

    void deleteCustomer(@JsonRpcParam("id") Integer id);

    List<Customer> findByName(@JsonRpcParam("name") String name);
    List<Customer> findBySurname(@JsonRpcParam("surname") String surname);
    List<Customer> findByNameAndSurname(@JsonRpcParam("name") String name, @JsonRpcParam("surname") String surname);

    Customer findBySocialSecurityNumber(@JsonRpcParam("socialSecurityNumber") Integer socialSecurityNumber);
    Customer findByEmail(@JsonRpcParam("email") String email);

    List<Customer> listAllCustomers();

    Customer updateCustomer(@JsonRpcParam("id") Integer id,
            @JsonRpcParam("initials") String initials, @JsonRpcParam("name") String name, @JsonRpcParam("surname") String surname,
            @JsonRpcParam("birthDate") Date birthDate, @JsonRpcParam("socialSecurityNumber") Integer socialSecurityNumber,
            @JsonRpcParam("address") String address, @JsonRpcParam("telephoneNumber") String telephoneNumber, @JsonRpcParam("email") String email);

    Customer updateName(@JsonRpcParam("id") Integer id, @JsonRpcParam("initials") String initials, @JsonRpcParam("name") String name, @JsonRpcParam("surname") String surname);
    Customer updateAddress(@JsonRpcParam("id") Integer id, @JsonRpcParam("address") String address);
    Customer updateTelephoneNumber(@JsonRpcParam("id") Integer id, @JsonRpcParam("telephoneNumber") String telephoneNumber);
    Customer updateEmail(@JsonRpcParam("id") Integer id, @JsonRpcParam("email") String email);

}
