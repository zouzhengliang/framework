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
 * @param <K> type of key
 * @param <D> type of data entry
 */
public abstract class GridPage<K, D extends GridData<D>> {

    // the parent dataset
    final GridDataSet<K, D> parent;

    // partition ID, can be string, integral or null (mere partition)
    final K id;

    GridPage(final GridDataSet<K, D> parent, K id) {
        this.parent = parent;
        this.id = id;
    }

    public K key() {
        return id;
    }

    abstract D get(K key);

    abstract K put(K key, D data);

    public D update(K key, Updater<D> updater) {
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

    GridQuery<K, D> newQuery(Critera<D> filter) {
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