package honours.ing.banq.event;

import honours.ing.banq.Application;
import honours.ing.banq.time.TimeServiceImpl;
import honours.ing.banq.variables.VarService;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * An interceptor for the API calls which runs the events if they are due.
 * @author Jeffrey Bakker
 * @since 5-8-17
 */
@Aspect
@Component
public class EventInterceptor implements ApplicationContextAware {

    private HashMap<Event, Long> events;

    private ApplicationContext context;
    private VarService vars;

    private Set<Event> nextEvents;
    private long nextEventTime;

    private AtomicBoolean running;

    /**
     * Initializes the {@link EventInterceptor}.
     * @param varService the variable service
     */
    @SuppressWarnings("unchecked")
    @Autowired
    public EventInterceptor(VarService varService) {
        events = new HashMap<>();
        nextEvents = new HashSet<>();

        vars = varService;
        running = new AtomicBoolean(false);
    }

    /**
     * Initializes the interceptor and finds all of the events.
     */
    private void init() {
        // Scan for Events
        ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(
                false);

        provider.addIncludeFilter((metadataReader, metadataReaderFactory) -> {
            try {
                return Event.class.isAssignableFrom(Class.forName(metadataReader.getClassMetadata().getClassName()));
            } catch (ClassNotFoundException e) {
                return false;
            }
        });
        Set<BeanDefinition> beans = provider.findCandidateComponents("honours.ing");

        Logger log = Application.getLogger();
        log.info("Found " + beans.size() + " events");;

        // List all events
        for (BeanDefinition bd : beans) {
            Class<? extends Event> beanClass = null;
            try {
                beanClass = (Class<? extends Event>) Class.forName(bd.getBeanClassName());
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            //noinspection ConstantConditions
            log.info("Found the event: " + beanClass.getSimpleName());

            Event event = context.getBean(beanClass);

            events.put(event, 0L);
        }

        calcTimers();
    }

    /**
     * Intercepts an API call.
     */
    @Before("@within(com.googlecode.jsonrpc4j.spring.AutoJsonRpcServiceImpl)")
    public void intercept() {
        synchronized (running) {
            if (running.get()) {
                return;
            }

            running.set(true);
        }
        try {
            while (TimeServiceImpl.currentTimeMillis() > nextEventTime) {
                vars.setVariable("lastEvent", "" + nextEventTime);

                for (Event e : nextEvents) {
                    e.execute(nextEventTime);
                    events.put(e, e.nextIteration(nextEventTime));
                }

                calcNextEvent();
            }
        } finally {
            running.set(false);
        }
    }

    /**
     * Calculates the first iterations for all of the events.
     */
    public void calcTimers() {
        long lastEvent = Long.parseLong(vars.getVariable(
                "lastEvent",
                "" + TimeServiceImpl.currentTimeMillis()));

        for (Event e : events.keySet()) {
            events.put(e, e.nextIteration(lastEvent));
        }

        calcNextEvent();
    }

    /**
     * Calculates the first next event (or events) that have to be run.
     */
    private void calcNextEvent() {
        // First clear the events
        nextEvents.clear();

        for (Event e : events.keySet()) {
            long time = events.get(e);

            if (nextEvents.isEmpty()) {
                // If there is no event queued yet, this event should be queued
                nextEvents.add(e);
                nextEventTime = time;
            } else if (time == nextEventTime) {
                // If this event's next iteration has the same time as the other queued ones, add this one to the set
                nextEvents.add(e);
            } else if (time < nextEventTime) {
                // If this event's next iteration has a time earlier that the already queued event(s), clear the set and
                // only add this one
                nextEvents.clear();
                nextEvents.add(e);
                nextEventTime = time;
            }
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        this.context = context;
        init();
    }
}
