package honours.ing.banq.database;

import honours.ing.banq.Log;

import java.sql.*;
import java.util.HashMap;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * @author Kevin Witlox, Jeffrey Bakker
 */
public class Database implements Runnable {

    private final String TAG = getClass().getSimpleName();

    private static final int QUEUE_SIZE = 128;

    private static HashMap<String, Database> databases = new HashMap<>();

    private ArrayBlockingQueue<DatabaseQuery> queries;
    private Connection connection;
    private Thread thread;
    private String fileName;

    private boolean running;

    /**
     * Initializes a new <code>Database</code> object that will manage the given database file.
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
        open();

        // Create variables
        DatabaseQuery query;

        // Loop until we are not running anymore and there are no more queries to execute
        while (running || !queries.isEmpty()) {
            try {
                // Take a query from the queue of queries
                query = queries.take();

                // Create the statement
                Statement statement = null;
                try {
                    statement = connection.createStatement();
                } catch (SQLException e) {
                    Log.e(TAG, "Could not create statement.", e);
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
                // This thread might be interrupted while retrieving a new query from the queue,
                // but we do not need
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
     * Add a single <code>DatabaseQuery</code> to the queue.
     * The query will be dropped if the queue is full.
     */
    public void addQuery(DatabaseQuery query) {
        if (queries.remainingCapacity() > 1) {
            queries.add(query);
        } else {
            System.err.println("Database Queue is full");
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
     *
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
