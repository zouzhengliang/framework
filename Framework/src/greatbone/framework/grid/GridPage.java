package greatbone.framework.grid;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * An abstract data page, conceptually containing data entries belonged.
 *
 * @param <D> type of data object
 */
public abstract class GridPage<D extends GridData<D>> {

    // the parent dataset
    final GridDataSet<D> parent;

    // the page id
    final String id;

    // for the key-generating algorithm
    final AtomicInteger serial;

    GridPage(final GridDataSet<D> parent, String id) {
        this.parent = parent;
        this.id = id;
        this.serial = new AtomicInteger(0);
    }

    public String id() {
        return id;
    }

    public abstract D get(String key);

    public abstract D put(String key, D data);

    public abstract D search(Critera<D> filter);

    /**
     * create a key aocrdding to the rule specific to this partion
     *
     * @return
     */
    protected String newKey() {
        return null;
    }

//    int meet(K akey) {
//        int cmp = akey.compareTo(id);
//        if (cmp >= 0 && akey.startsWith(id)) {
//            return 0;
//        }
//        return cmp;
//    }

}