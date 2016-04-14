package greatbone.framework.grid;

/**
 * The partitioning is based on integral key ranges
 * <p/>
 * no loading support
 */
public abstract class GridHashedDataSet<D extends GridData<D>> extends GridDataSet<D> {

    protected GridHashedDataSet(GridUtility grid) {
        super(grid, 8);
    }

    @Override
    protected final void load(String arg) {
    }

    @Override
    GridShard<D> shard(String key) {
        return null;
    }

    public D get(Object key) {
        return null;
    }

    public void put(Putter putter) {

    }

    public D getAll(Critera conf) {
        return null;
    }


}
