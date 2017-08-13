package honours.ing.banq.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

/**
 * A class for easily getting the system's {@link ApplicationContext}.
 * @author Jeffrey Bakker
 * @since 5-8-17
 */
@Service
public class ContextWrapper {

    private static ApplicationContext context;

    @Autowired
    public ContextWrapper(ApplicationContext ac) {
        context = ac;
    }

    /**
     * Returns this system's {@link ApplicationContext}.
     * @return the {@link ApplicationContext}
     */
    public static ApplicationContext getContext() {
        return context;
    }

}
