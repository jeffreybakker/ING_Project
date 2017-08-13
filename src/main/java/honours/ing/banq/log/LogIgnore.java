package honours.ing.banq.log;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * All methods with this annotation will be ignored in the system logs.
 * @author Jeffrey Bakker
 * @since 6-8-17
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface LogIgnore {
}
