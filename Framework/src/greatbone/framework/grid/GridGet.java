package greatbone.framework.grid;

import java.util.concurrent.ForkJoinTask;

/**
 * A get task that works on a particular page
 */
class GridGet<D extends GridData<D>> extends ForkJoinTask<D> {

    // the target abstract page
    final GridPage<D> page;

    // the key to find out
    final String key;

    // the result data entry, can be null
    D result;

    GridGet(GridPage<D> page, String key) {
        this.page = page;
        this.key = key;
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
        result = page.get(key);
        return true;
    }

}
