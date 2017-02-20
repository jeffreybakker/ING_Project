package honours.ing.banq.database;

import honours.ing.banq.Log;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.Assert.*;

/**
 * @author Kevin Witlox
 */
public class DatabaseTest {

    private final ByteArrayOutputStream outstream = new ByteArrayOutputStream();

    private Database database;

    @Before
    public void before() throws Exception {
        System.setOut(new PrintStream(outstream));
        Log.start();
    }

    @Test
    public void getDatabase() throws Exception {
        database = Database.getDatabase("nonexistant.sqlite");
        assertTrue(readConsole().contains(Database.ERR_CREATE_CONNECTION));

        database = Database.getDatabase(Databases.ING.getFileName());
        database.addQuery(new DatabaseQuery("CREATE TABLE test(key INTEGER, value TEXT);", null));
        database.addQuery(new DatabaseQuery("INSERT INTO test VALUES (0, 'test')", null));
        database.addQuery(new DatabaseQuery("SELECT * FROM test WHERE key=0", resultSet -> {
            try {
               resultSet.next();
               assertEquals("test", resultSet.getString(2));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }));
        database.addQuery(new DatabaseQuery("DROP TABLE IF EXISTS test", null));
        database.stop(true);
    }

    @After
    public void after() {
        Log.stop();
        System.setOut(null);
    }

    public String readConsole() throws Exception {
        String res = outstream.toString();
        while (res.equals("")) {
            Thread.sleep(5);
            res = outstream.toString();
        }

        return res;
    }

}