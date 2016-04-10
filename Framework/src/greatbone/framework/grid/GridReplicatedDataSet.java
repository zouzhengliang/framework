package greatbone.framework.grid;

/**
 * A dataset is replicated onto each and every member node.
 */
public abstract class GridReplicatedDataSet<K, D extends GridData<D>> extends GridDataSet<K, D> {

    static final int DEFAULT_CAP = 1024 * 4;

    protected GridReplicatedDataSet(GridUtility grid) {
        super(grid, 1);

        // add the only page
        int cap = DEFAULT_CAP;
        if (config != null) {
            String pagecap = config.getAttribute("pagecap");
            if (!pagecap.isEmpty()) {
                cap = Integer.parseInt(pagecap);
            }
        }
//        insert(new GridLocalPage<>(this, null, cap));
    }

    @Override
    GridPage<K, D> shard(K key) {
        return shard(0); // returns the only partition
    }

}
