package greatbone.framework.grid;

import java.io.Serializable;

/**
 */
public interface Critera<P> extends Serializable {

    boolean test(P value);

}
