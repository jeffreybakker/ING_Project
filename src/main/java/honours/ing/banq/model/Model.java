package honours.ing.banq.model;

import honours.ing.banq.database.DatabaseQuery;
import honours.ing.banq.database.annotation.DBIgnored;

/**
 * @author Jeffrey Bakker
 */
public abstract class Model {

    @DBIgnored
    private static int LAST_ID = -1;

    protected int id;

    /**
     * Inserts this model's instance into the database.
     */
    protected void insert() {
        DatabaseQuery query = ModelQuery.getInsertQuery(this);
        // TODO: Enqueue query
    }

    /**
     * Updates this model's instance in the database to match the data in this object.
     */
    public void save() {
        DatabaseQuery query = ModelQuery.getUpdateQuery(this);
        // TODO: Enqueue query
    }

    /**
     * Generates a new unique ID for this model's instance.
     * @return a new unique ID for this model
     */
    protected static int genID() {
        if (LAST_ID == -1) {
            final boolean[] done = {false};

            DatabaseQuery query = new DatabaseQuery(String.format(
                    "SELECT id FROM %s ORDER BY id DESC LIMIT 1",
                    "TableName"), // TODO: Update table-name
                    resultSet -> {
                        synchronized (done) {
                            // TODO: update LAST_ID
                            done[0] = true;
                            done.notify();
                        }
                    });

            // TODO: Enqueue query

            while (!done[0]) {
                try {
                    synchronized (done) {
                        done.wait();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            throw new UnsupportedOperationException("This method has not been implemented yet");
        }

        LAST_ID++;
        return LAST_ID;
    }

}
