package greatbone.framework.grid;

/**
 * cache of underlying file system files.
 */
public class GridPartedFileSet extends GridFileSet {

    protected GridPartedFileSet(GridUtility manager, int capacity) {
        super(manager, capacity);
    }
}
