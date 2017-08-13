package honours.ing.banq.account;

import com.googlecode.jsonrpc4j.JsonRpcParam;
import com.googlecode.jsonrpc4j.JsonRpcService;
import honours.ing.banq.error.InvalidParamValueError;
import honours.ing.banq.account.bean.NewAccountBean;
import honours.ing.banq.account.bean.OverdraftBean;
import honours.ing.banq.error.NotAuthorizedError;

/**
 * This service provides the methods for the Account module.
 * @author Jeffrey Bakker
 * @since 17-4-17
 */
@JsonRpcService("/api/account")
public interface BankAccountService {

    /**
     * Open a bank account. This method also creates a <code>Customer</code> and a <code>Card</code> or this bank
     * account.
     * @param name            the name of the customer
     * @param surname         the surname of the customer
     * @param initials        the initials of the customer
     * @param dob             the date of birth of the customer
     * @param ssn             the social security number of the customer
     * @param address         the address of the customer
     * @param telephoneNumber the telephone number of the customer
     * @param email           the email address of the customer
     * @param username        the preferred username of the customer
     * @param password        the password of the customer
     * @return an object containing basic information about the newly created bank account
     * @throws InvalidParamValueError if one or more of the parameters has an invalid value
     */
    NewAccountBean openAccount(
            @JsonRpcParam("name") String name, @JsonRpcParam("surname") String surname, @JsonRpcParam("initials")
            String initials,
            @JsonRpcParam("dob") String dob, @JsonRpcParam("ssn") String ssn,
            @JsonRpcParam("address") String address, @JsonRpcParam("telephoneNumber") String telephoneNumber,
            @JsonRpcParam("email") String email, @JsonRpcParam("username") String username, @JsonRpcParam("password")
                    String password)
            throws InvalidParamValueError;

    /**
     * Open a additional account for an existing logged in customer. Also creates a <code>Card</code> for this account.
     * @param authToken the authentication token, obtained with <code>getAuthToken</code>
     * @return an object containing basic information about the newly created bank account
     * @throws NotAuthorizedError if something went wrong with the authentication module
     */
    NewAccountBean openAdditionalAccount(@JsonRpcParam("authToken") String authToken) throws NotAuthorizedError;

    /**
     * Closes the bank account.
     * @param authToken the authentication token of the logged in user
     * @param iBAN the iBAN of the account that should be closed
     * @return an empty dictionary
     * @throws NotAuthorizedError if the user is not authorized to close the account
     * @throws InvalidParamValueError if some of the parameters are invalid, please see the message
     */
    Object closeAccount(@JsonRpcParam("authToken") String authToken, @JsonRpcParam("iBAN") String iBAN)
            throws NotAuthorizedError, InvalidParamValueError;

    /**
     * Method that asks the server for the overdraft limit of a bank account.
     * @param authToken the authentication token, obtained with {@code getAuthToken}
     * @param iBAN      the number of the bank account
     * @return a dictionary containing the overdraft limit
     * @throws NotAuthorizedError if the user is not authorized to see the overdraft limit on this account
     * @throws InvalidParamValueError if one of the parameters was invalid
     */
    OverdraftBean getOverdraftLimit(@JsonRpcParam("authToken") String authToken, @JsonRpcParam("iBAN") String iBAN)
            throws NotAuthorizedError, InvalidParamValueError;

    /**
     * Method that sets the overdraft limit for a bank account.
     * @param authToken      the authentication token, obtained with {@code getAuthToken}
     * @param iBAN           the number of the bank account
     * @param overdraftLimit the new overdraft limit of the account
     * @return an empty dictionary if successful
     * @throws NotAuthorizedError if the user is not authorized to set the overdraft limit on this account
     * @throws InvalidParamValueError if one of the parameters was invalid
     */
    Object setOverdraftLimit(
            @JsonRpcParam("authToken") String authToken, @JsonRpcParam("iBAN") String iBAN,
            @JsonRpcParam("overdraftLimit") double overdraftLimit)
            throws NotAuthorizedError, InvalidParamValueError;

    Object openSavingsAccount(@JsonRpcParam("authToken") String authToken, @JsonRpcParam("iBAN") String iBAN)
            throws InvalidParamValueError, NotAuthorizedError;

    Object closeSavingsAccount(@JsonRpcParam("authToken") String authToken, @JsonRpcParam("iBAN") String iBAN)
            throws InvalidParamValueError, NotAuthorizedError;

}