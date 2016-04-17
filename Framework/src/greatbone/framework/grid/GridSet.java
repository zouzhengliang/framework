package greatbone.framework.grid;

/**
 * A dataset or fileset.
 */
public abstract class GridSet {

    // the container grid instance
    final GridUtility grid;

    final String key;

    GridSet(GridUtility grid) {
        this.grid = grid;
        this.key = getClass().getSimpleName().toLowerCase(); // from class name
    }

    abstract void flush();
}
