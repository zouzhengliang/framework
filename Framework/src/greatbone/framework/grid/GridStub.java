package greatbone.framework.grid;

/**
 * A stub works as proxy of a remote parge.
 */
class GridStub<D extends GridData<D>> extends GridShard<D> {

    // cache
    GridClient client;

    GridStub(GridDataSet<D> parent, String id, GridClient client) {
        super(parent, id);

        this.client = client;
    }

    @Override
    D get(String key) {
        return null;
    }

    @Override
    D put(String key, D dat) {
        return null;
    }

    @Override
    D query(Critera<D> filter) {
        return client.query(parent.key, id, filter);
    }

}
