package greatbone.framework.grid;

import java.util.List;

/**
 * A dataset that is sharded into pages on different member nodes according to key parts.
 */
public abstract class GridPartedDataSet<D extends GridData<D>> extends GridDataSet<D> {

    protected GridPartedDataSet(GridUtility parent) {
        super(parent, 32);
    }

    @Override
    public GridPage<D> shard(String key) {
        return null;
    }

    protected List<GridPage<D>> targets(String keypre) {
        return null;
    }



}
