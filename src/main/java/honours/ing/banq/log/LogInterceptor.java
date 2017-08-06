package honours.ing.banq.log;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author jeffrey
 * @since 6-8-17
 */
@Aspect
@Component
public class LogInterceptor implements ApplicationContextAware {

    private ApplicationContext context;
    private LogService log;

    private final ThreadLocal<AtomicBoolean> running;

    @Autowired
    public LogInterceptor(LogService logService) {
        this.log = logService;

        running = ThreadLocal.withInitial(() -> new AtomicBoolean(false));
    }

    @Around("within(honours.ing..*) && @annotation(org.springframework.transaction.annotation.Transactional)")
    public Object intercept(ProceedingJoinPoint pjp) throws Throwable {
        boolean mayLog = false;

        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();
        LogIgnore annotation = method.getAnnotation(LogIgnore.class);

        if (annotation == null) {
            synchronized (running) {
                if (!running.get().get()) {
                    running.get().set(true);
                    mayLog = true;
                }
            }
        }

        try {
            Object res = pjp.proceed();

            if (mayLog) {
                log.log(String.format("%s%s", method.getName(), Arrays.deepToString(pjp.getArgs())));
            }

            return res;
        } catch (Throwable t) {
            if (mayLog) {
                log.log(String.format("[%s] %s%s", t.getClass().getSimpleName(), method.getName(), Arrays.deepToString(pjp.getArgs())));
            }
            throw t;
        } finally {
            if (mayLog) {
                running.get().set(false);
            }
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

    }
}
