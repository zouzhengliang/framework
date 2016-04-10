package greatbone.framework.grid;

/**
 * The partitioning is based on integral key ranges
 */
public abstract class GridRangedDataSet<K, D extends GridData<D>> extends GridDataSet<K, D> {

    protected GridRangedDataSet(GridUtility parent) {
        super(parent, 16);
    }

    @Override
    GridPage<K, D> shard(K key) {


        return null;
    }


}
