package greatbone.framework.grid;

import java.lang.annotation.*;

/**
 *
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ReadPolicy {

    boolean through() default true;

}
