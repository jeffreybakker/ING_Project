package honours.ing.banq.database;

import honours.ing.banq.Log;

import java.sql.*;
import java.util.HashMap;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * @author Kevin Witlox, Jeffrey Bakker
 */
public class Database implements Runnable {

    public static final String ERR_CREATE_CONNECTION = "Could not create connection to database";
    public static final String ERR_CREATE_STATEMENT = "Could not create statement";

    private static final String COMMAND_STOP = "stop";
    private static final int QUEUE_SIZE = 128;

    private final String TAG = getClass().getSimpleName();

    private static HashMap<String, Database> databases = new HashMap<>();

    private ArrayBlockingQueue<DatabaseQuery> queries;
    private Connection connection;
    private Thread thread;
    private String fileName;

    private boolean running;

    /**
     * Initializes a new <code>Database</code> object that will manage the given database file.
     * Call <code>getDatabase()</code> to create a new <code>Database</code>.
     *
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
        if (!open()) {
            return;
        }

        // Create variables
        DatabaseQuery query;

        // Loop until we are not running anymore and there are no more queries to execute
        while (running || !queries.isEmpty()) {
            try {
                // Take a query from the queue of queries
                query = queries.take();

                if (query.getQuery().equals(COMMAND_STOP)) {
                    continue;
                }

                // Create the statement
                Statement statement = null;
                try {
                    statement = connection.createStatement();
                } catch (SQLException e) {
                    Log.e(TAG, ERR_CREATE_STATEMENT, e);
                    System.exit(1);
                }

                // Execute the query
                try {
                    DatabaseCallback callback = query.getCallback();
                    statement.execute(query.getQuery());
                    ResultSet resultSet = statement.getResultSet();

                    int updateCount = statement.getUpdateCount();
                    if (updateCount != -1) {
                        Log.i(TAG, "Query executed, update count: " + updateCount);
                    }

                    // Callback
                    if (callback != null) {
                        callback.onCallback(resultSet);
                    }

                    if (statement != null) {
                        statement.close();
                    }
                } catch (SQLException e) {
                    Log.e(TAG, "Could not execute query.", e);
                    System.exit(1);
                } finally {
                    try {
                        statement.close();
                    } catch (SQLException e) {
                        Log.e(TAG, "Could not close statement.");
                    }
                }
            } catch (InterruptedException ignored) {
                Log.i(TAG, "Database take() interrupted");
                // This thread might be interrupted while retrieving a new query from the queue,
                // but we do not need to
                // handle that
            }
        }

        close();
    }

    /**
     * Opens the connection with the database.
     */
    private boolean open() {
        try {
            connection = DriverManager.getConnection(fileName);
            return true;
        } catch (SQLException e) {
            Log.e(TAG, ERR_CREATE_CONNECTION);
            return false;
        }
    }

    /**
     * Add a single <code>DatabaseQuery</code> to the queue.
     * The query will be dropped if the queue is full.
     */
    public void addQuery(DatabaseQuery query) {
        if (queries.remainingCapacity() >= 1) {
            queries.add(query);
        } else {
            Log.e(TAG, "Database Queue is full");
        }
    }

    /**
     * Closes and saves the database.
     */
    private void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            Log.e(TAG, "Could not close database connection.");
        }
    }

    /**
     * Starts the database.
     */
    private synchronized void start() {
        if (running) {
            throw new IllegalStateException("Database is already running");
        }

        thread = new Thread(this, "Database");
        running = true;
        thread.start();
    }

    /**
     * Stops and saves the database asynchronously.
     */
    public void stop() {
        stop(false);
    }

    /**
     * Stops and saves the database.
     *
     * @param wait when <code>true</code> this method will wait until the thread is finished
     */
    public synchronized void stop(boolean wait) {
        if (!running) {
            throw new IllegalStateException("Database is not running (anymore)");
        }

        running = false;
        addQuery(new DatabaseQuery(COMMAND_STOP, null));

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
     * Returns the <code>Database</code> object for the given <code>fileName</code> and opens a new
     * database connection if there was no open connection yet.
     *
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
     *
     * @return <code>true</code> if the database is running
     */
    public boolean isRunning() {
        return running;
    }

}
