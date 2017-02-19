package honours.ing.banq.database;

/**
 * @author Jeffrey Bakker, Kevin Witlox
 */
public class DatabaseQuery {

    private String query;
    private DatabaseCallback callback;

    public DatabaseQuery(String query, DatabaseCallback callback) {
        this.query = query;
        this.callback = callback;
    }

    public String getQuery() {
        return query;
    }

    public DatabaseCallback getCallback() {
        return callback;
    }
}
