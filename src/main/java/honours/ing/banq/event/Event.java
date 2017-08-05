package honours.ing.banq.event;

/**
 * @author jeffrey
 * @since 5-8-17
 */
public interface Event {

    void execute(long time);
    long nextIteration(long lastIteration);

}
