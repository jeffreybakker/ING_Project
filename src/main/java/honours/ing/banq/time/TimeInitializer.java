package honours.ing.banq.time;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Kevin Witlox
 * @since 13-7-2017.
 */
@Component
public class TimeInitializer {

    @Autowired
    private TimeRepository timeRepository;

    @EventListener(ContextRefreshedEvent.class)
    public void contextRefreshedEvent() {
        List<Time> times = timeRepository.findAll();
        if (times != null && times.size() == 0) {
            Time initialTime = new Time(0);
            timeRepository.save(initialTime);
        }
    }

}
