package honours.ing.banq.log;

import honours.ing.banq.time.TimeServiceImpl;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * The model for a log message that is saved in the database.
 * @author Jeffrey Bakker
 * @since 6-8-17
 */
@Entity
public class Log {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private long timeStamp;
    private String eventLog;

    /**
     * @deprecated empty constructor for Hibernate ORM
     */
    @Deprecated
    public Log() { }

    /**
     * Creates a new {@link Log} object with the given parameters.
     * @param message the message for the log
     */
    public Log(String message) {
        this(TimeServiceImpl.currentTimeMillis(), message);
    }

    /**
     * Creates a new {@link Log} object with the given parameters.
     * @param timeStamp the timeStamp for the log
     * @param message   the message for the log
     */
    public Log(long timeStamp, String message) {
        this.timeStamp = timeStamp;
        this.eventLog = message;
    }

    /**
     * Returns the {@link Log}'s ID.
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * Returns the time stamp in milliseconds.
     * @return the time stamp
     */
    public long getTimeStamp() {
        return timeStamp;
    }

    /**
     * Sets the time for the time stamp.
     * @param timeStamp the time in milliseconds
     */
    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    /**
     * Returns the message for the log.
     * @return the message
     */
    public String getMessage() {
        return eventLog;
    }

    /**
     * Sets the message for the log.
     * @param message the message
     */
    public void setMessage(String message) {
        this.eventLog = message;
    }
}
