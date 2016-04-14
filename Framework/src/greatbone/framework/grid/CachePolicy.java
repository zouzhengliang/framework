package greatbone.framework.grid;

import java.lang.annotation.*;

/**
 * CachePolicy specifies cache attributes for a dataset. It can read though, write through, write behind
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface CachePolicy {

    /**
     * Whether or not to load from the underlying database if a cached entry missed of a read operation.
     */
    boolean load() default false;

    /**
     * The number of seconds after that an update will be saved to underlying database
     */
    int save() default -1;

    /**
     * To keep a copy of data pages on the next node.
     */
    boolean copy() default false;

}
