package honours.ing.banq.log;

import honours.ing.banq.time.TimeServiceImpl;

import javax.persistence.*;

/**
 * The model for a log message that is saved in the database.
 * @author Jeffrey Bakker
 * @since 6-8-17
 */
@Entity
public class Log {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;

    private long timeStamp;

    @Column(columnDefinition = "TEXT")
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Log log = (Log) o;

        if (timeStamp != log.timeStamp) return false;
        return id != null ? id.equals(log.id) : log.id == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (int) (timeStamp ^ (timeStamp >>> 32));
        return result;
    }
}
