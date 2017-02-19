package honours.ing.banq.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * @author Kevin Witlox, Jeffrey Bakker
 */
public class Database implements Runnable {

    private static final int QUEUE_SIZE = 128;

    private static HashMap<String, Database> databases = new HashMap<>();

    private ArrayBlockingQueue<DatabaseQuery> queries;
    private Connection connection;
    private Thread thread;
    private String fileName;

    private boolean running;

    /**
     * Initializes a new <code>Database</code> object that will manage the given database file.
     * @param fileName the path to the file of the database to manage
     */
    private Database(String fileName) {
        this.fileName = fileName;
        this.running = false;

        queries = new ArrayBlockingQueue<>(QUEUE_SIZE, true);

    }

    @Override
    public void run() {
        // First open the database
        open();

        // Create variables
        DatabaseQuery query;

        // Loop until we are not running anymore and there are no more queries to execute
        while (running || !queries.isEmpty()) {
            try {
                // Take a query from the queue of queries
                query = queries.take();

                // Execute the query
                query.onExecute(this);
            } catch (InterruptedException ignored) {
                // This thread might be interrupted while retrieving a new query from the queue, but we do not need
                // handle that
            }
        }

        close();
    }

    /**
     * Opens the connection with the database.
     */
    private void open() {
        try {
            connection = DriverManager.getConnection(fileName);
        } catch (SQLException e) {
            System.err.println("Could not create connection to database");
        }
    }

    /**
     * Saves the data in the database to the file.
     */
    public void save() {
        // TODO: Save the database
    }

    /**
     * Closes and saves the database.
     */
    private void close() {
        // First save the changes in the database
        save();

        // Then close the database
        // TODO: Close the database
    }

    /**
     * Starts the database.
     */
    public synchronized void start() {
        if (running) {
            throw new IllegalStateException("Database is already running");
        }

        thread = new Thread(this, "Database");
        running = true;
        thread.run();
    }

    /**
     * Stops and saves the database asynchronously.
     */
    public void stop() {
        stop(false);
    }

    /**
     * Stops and saves the database.
     * @param wait when <code>true</code> this method will wait until the thread is finished
     */
    public synchronized void stop(boolean wait) {
        if (!running) {
            throw new IllegalStateException("Database is not running (anymore)");
        }

        running = false;

        if (wait) {
            try {
                thread.join();
                thread = null;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Returns the <code>Database</code> object for the given <code>fileName</code> and opens a new database connection
     * if there was no open connection yet.
     * @param fileName the path to the database file
     * @return the <code>Database</code> object for the given <code>fileName</code>
     */
    public static synchronized Database getDatabase(String fileName) {
        Database res = databases.get(fileName);
        if (res != null) {
            return res;
        }

        res = new Database(fileName);
        res.start();

        databases.put(fileName, res);
        return res;
    }

    /**
     * Returns whether the database is running or not.
     * @return <code>true</code> if the database is running
     */
    public boolean isRunning() {
        return running;
    }

}
