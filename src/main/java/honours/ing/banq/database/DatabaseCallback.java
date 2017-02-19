package honours.ing.banq.database;

import java.sql.ResultSet;

/**
 * Created by Kevin Witlox on 19-2-2017.
 */
public interface DatabaseCallback {

    void onCallback(ResultSet resultSet);

}
