package greatbone.framework.grid;

/**
 * This works as proxy of a remote parge.
 */
class GridRemotePage<D extends GridData<D>> extends GridPage<D> {

    // cache
    GridClient client;

    GridRemotePage(GridDataSet<D> parent, String id, GridClient client) {
        super(parent, id);

        this.client = client;
    }

    @Override
    D get(String key) {
        return null;
    }

    @Override
    String put(String key, D data) {
        return null
                ;
    }

    @Override
    D search(Critera<D> filter) {
        return client.query(parent.key, id, filter);
    }

}
