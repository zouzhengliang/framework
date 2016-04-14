package greatbone.framework.grid;

import java.util.concurrent.ForkJoinTask;

/**
 * A query task that works on a particular page
 */
class GridSearch<D extends GridData<D>> extends ForkJoinTask<D> {

    final GridPage<D> page;

    final Critera<D> filter;

    final boolean ascending;

    // return data
    D result;


    GridSearch(GridPage<D> page, Critera<D> filter, boolean ascending) {
        this.page = page;
        this.filter = filter;
        this.ascending = ascending;
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
