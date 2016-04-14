package greatbone.framework.grid;

import java.util.function.Predicate;

/**
 * An abstract data page, conceptually containing data entries belonged.
 *
 * @param <D> type of data object
 */
abstract class GridPage<D extends GridData<D>> {

    // the parent dataset
    final GridDataSet<D> parent;

    // the page id
    final String id;

    GridPage(final GridDataSet<D> parent, String id) {
        this.parent = parent;
        this.id = id;
    }

    public String id() {
        return id;
    }

    abstract D get(String key);

    abstract D put(String key, D data);

    abstract D query(Critera<D> filter);

    public D update(String key, Updater<D> updater) {
        return null;
    }

    public D update(Predicate<D> condition, Updater<D> updater) {
        return null;
    }


    /**
     * create a key aocrdding to the rule specific to this partion
     *
     * @return
     */
    protected String newKey() {
        return null;
    }

    GridQuery<D> newQuery(Critera<D> filter) {
        return new GridQuery<>(this, filter);
    }

//    int meet(K akey) {
//        int cmp = akey.compareTo(id);
//        if (cmp >= 0 && akey.startsWith(id)) {
//            return 0;
//        }
//        return cmp;
//    }

}