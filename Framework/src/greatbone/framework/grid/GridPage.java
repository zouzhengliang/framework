package greatbone.framework.grid;

import java.util.function.Predicate;

/**
 * data page
 * <p/>
 * lock-free
 * sequential in addition order
 * query
 * aggregation
 * <p/>
 * <p/>
 * fork/join on large data partition
 *
 * @param <D> type of data entry
 */
public abstract class GridPage<D extends GridData<D>> {

    // the parent dataset
    final GridDataSet<D> parent;

    // partition ID, can be string, integral or null (mere partition)
    final String id;

    GridPage(final GridDataSet<D> parent, String id) {
        this.parent = parent;
        this.id = id;
    }

    public Object key() {
        return id;
    }

    abstract D get(String key);

    abstract Object put(String key, D data);

    public D update(String key, Updater<D> updater) {
        return null;
    }

    public D update(Predicate<D> condition, Updater<D> updater) {
        return null;
    }

    abstract D search(Critera<D> filter);


    /**
     * create a key aocrdding to the rule specific to this partion
     *
     * @return
     */
    protected String newKey() {
        return null;
    }

    GridQuery<Object, D> newQuery(Critera<D> filter) {
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