package honours.ing.banq.event;

/**
 * An event that can be triggered at a given moment in time.
 * @author Jeffrey Bakker
 * @since 5-8-17
 */
public interface Event {

    /**
     * Handles the event.
     * @param time the time for this event
     */
    void execute(long time);

    /**
     * Calculates the next iteration of this event.
     * @param lastIteration the last iteration of this event
     * @return the next iteration
     */
    long nextIteration(long lastIteration);

}
