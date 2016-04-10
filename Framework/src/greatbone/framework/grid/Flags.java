package greatbone.framework.grid;

import java.lang.annotation.*;

/**
 * For direct conversion of data (as josn part) in a distributed query
 */
@Documented
@Target({ElementType.TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Flags {

    int value();

}
