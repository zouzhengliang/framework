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
     * whether to make a backup copy on the next node.
     */
    boolean duplicate() default false;

}
