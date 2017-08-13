package honours.ing.banq.time;

import com.googlecode.jsonrpc4j.JsonRpcParam;
import com.googlecode.jsonrpc4j.JsonRpcService;
import honours.ing.banq.error.InvalidParamValueError;
import honours.ing.banq.time.bean.DateBean;

/**
 * This service lets the bank operators simulate time, this way they can test if time dependent events happen correctly.
 * @author Jeffrey Bakker
 * @since 4-8-17
 */
@JsonRpcService("/api/time")
public interface TimeService {

    /**
     * Method that asks the server to process the passing of a specified number of days.
     * @param nrOfDays the number of days that should be simulated
     * @return an empty dictionary if successful
     * @throws InvalidParamValueError if the number of days is negative
     */
    Object simulateTime(@JsonRpcParam("nrOfDays") int nrOfDays) throws InvalidParamValueError;

    /**
     * Method that asks the server to restore the state to it's initial state and truncating all of the database tables.
     * @return an empty dictionary if successful
     */
    Object reset();

    /**
     * Saves the current time state.
     */
    void save();

    /**
     * Method that ask the server to get it's simulated date.
     * @return an object containing the simulated date
     */
    DateBean getDate();

}
