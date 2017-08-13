package honours.ing.banq.log;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * An interceptor that logs all API calls with their given parameters.
 * @author Jeffrey Bakker
 * @since 6-8-17
 */
@Aspect
@Component
public class LogInterceptor {

    private LogService log;

    private final ThreadLocal<AtomicBoolean> running;

    @Autowired
    public LogInterceptor(LogService logService) {
        this.log = logService;

        running = ThreadLocal.withInitial(() -> new AtomicBoolean(false));
    }

    /**
     * Logs all transactional API calls that are not annotated with {@link LogIgnore}.
     * @param pjp the {@link ProceedingJoinPoint} for the intercept
     * @return the response of the API call
     * @throws Throwable anything thrown by the API call
     */
    @Around("within(honours.ing..*) && @annotation(org.springframework.transaction.annotation.Transactional)")
    public Object intercept(ProceedingJoinPoint pjp) throws Throwable {
        boolean mayLog = false;

        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();
        LogIgnore annotation = method.getAnnotation(LogIgnore.class);

        // Check whether this API call should be logged or not
        if (annotation == null) {
            synchronized (running) {
                if (!running.get().get()) {
                    running.get().set(true);
                    mayLog = true;
                }
            }
        }

        try {
            // Run the API method
            Object res = pjp.proceed();

            // Log the call if it may be logged
            if (mayLog) {
                log.log(String.format("%s%s", method.getName(), Arrays.deepToString(pjp.getArgs())));
            }

            // Return the API method's response
            return res;
        } catch (Throwable t) {
            // If the API method threw an error or an exception, log it (if it may be logged)
            if (mayLog) {
                log.log(String.format("[%s] %s%s", t.getClass().getSimpleName(), method.getName(), Arrays.deepToString(pjp.getArgs())));
            }

            // Then throw that throwable
            throw t;
        } finally {
            if (mayLog) {
                running.get().set(false);
            }
        }
    }

}
