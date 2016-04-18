package greatbone.framework.grid;

import greatbone.framework.Configurable;
import greatbone.framework.Greatbone;
import org.w3c.dom.Element;

/**
 * A dataset or fileset.
 */
public abstract class GridSet implements Configurable {

    // the container grid instance
    final GridUtility grid;

    final String key;

    // configuration xml element
    final Element config;

    GridSet(GridUtility grid) {
        this.grid = grid;
        // derive the key
        Class p = getClass();
        this.key = p.getSimpleName().toLowerCase(); // from class name
        // derive the config tag
        while ((p = p.getSuperclass()) != GridSet.class) ;
        String tag = p.getSimpleName().substring(4).toLowerCase();
        this.config = Greatbone.childOf(grid.config, tag, key);
    }

    abstract void flush();

    @Override
    public Element config() {
        return config;
    }

}
