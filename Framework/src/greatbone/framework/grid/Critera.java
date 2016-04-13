package greatbone.framework.grid;

import java.io.Serializable;

/**
 *
 */
public interface Critera<D extends GridData<D>> extends Serializable {

    boolean test(D value);

}
