package greatbone.framework.grid;

/**
 * This works as proxy of a remote parge.
 */
class GridRemotePage<K, D extends GridData<D>> extends GridPage<K, D> {

    // cache
    GridClient client;

    GridRemotePage(GridDataSet<K, D> parent, K id, GridClient client) {
        super(parent, id);

        this.client = client;
    }

    @Override
    D get(K key) {
        return null;
    }

    @Override
    K put(K key, D data) {
        return null
                ;
    }

    @Override
    D search(Critera<D> filter) {
        return client.search(parent.key, id, filter);
    }

}
