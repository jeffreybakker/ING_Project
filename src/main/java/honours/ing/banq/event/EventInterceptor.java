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

/**
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

    @SuppressWarnings("unchecked")
    @Autowired
    public EventInterceptor(VarService varService) {
        events = new HashMap<>();
        nextEvents = new HashSet<>();

        vars = varService;
    }

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

    @Before("@within(com.googlecode.jsonrpc4j.spring.AutoJsonRpcServiceImpl)")
    public void intercept() {
        while (TimeServiceImpl.currentTimeMillis() > nextEventTime) {
            vars.setVariable("lastEvent", "" + nextEventTime);

            for (Event e : nextEvents) {
                e.execute(nextEventTime);
                events.put(e, e.nextIteration(nextEventTime));
            }

            calcNextEvent();
        }
    }

    public void calcTimers() {
        long lastEvent = Long.parseLong(vars.getVariable(
                "lastEvent",
                "" + TimeServiceImpl.currentTimeMillis()));

        for (Event e : events.keySet()) {
            events.put(e, e.nextIteration(lastEvent));
        }

        calcNextEvent();
    }

    private void calcNextEvent() {
        nextEvents.clear();

        for (Event e : events.keySet()) {
            long time = events.get(e);

            if (nextEvents.isEmpty()) {
                nextEvents.add(e);
                nextEventTime = time;
            } else if (time == nextEventTime) {
                nextEvents.add(e);
            } else if (time < nextEventTime) {
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
