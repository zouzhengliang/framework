package greatbone.framework.grid;

import java.util.concurrent.ForkJoinTask;

/**
 * A query task that works on a particular page
 */
class GridQuery<D extends GridData<D>> extends ForkJoinTask<D> {

    final GridPage<D> page;

    final Critera<D> filter;

    // return data
    D result;


    GridQuery(GridPage<D> page, Critera<D> filter) {
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
