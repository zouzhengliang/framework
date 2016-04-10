package greatbone.framework.grid;

import java.util.concurrent.ForkJoinTask;

/**
 * A put operation task that works on a particular page
 */
class GridPut<K, D extends GridData<D>> extends ForkJoinTask<D> {

    final GridPage<K, D> page;

    final Critera<D> filter;

    // return data
    D result;


    GridPut(GridPage<K, D> page, Critera<D> filter) {
        this.page = page;
        this.filter = filter;
    }

    @Override
    public D getRawResult() {
        return result;
    }

    @Override
    protected void setRawResult(D value) {
        this.result = value;
    }

    @Override
    protected boolean exec() {
        result = page.search(filter);
        return true;
    }

}
