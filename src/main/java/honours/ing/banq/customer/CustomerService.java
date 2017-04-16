package honours.ing.banq.customer;

import com.googlecode.jsonrpc4j.JsonRpcParam;
import com.googlecode.jsonrpc4j.JsonRpcService;

import java.util.Date;

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



}
