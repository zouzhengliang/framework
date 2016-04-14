package greatbone.framework.grid;

import greatbone.framework.util.SpinWait;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * A collection of element pages ordered by ID.
 */
class GridShardLot<D extends GridData<D>> extends SpinWait {

    // all element pages
    GridAbstractPage<D>[] elements;
    int count;

    @SuppressWarnings("unchecked")
    GridShardLot(int cap) {
        elements = new GridAbstractPage[cap];
    }

    void insert(GridAbstractPage<D> v) {

        GridAbstractPage<D>[] elems = elements;

        int low = 0;
        int high = count - 1;

        int r = 0;
        while (low <= high) {
            int mid = (low + high) >>> 1;
            GridAbstractPage<D> midv = elems[mid];
            int cmp = 0;//midv.meet(key);
            if (cmp < 0) low = mid + 1;
            else if (cmp > 0) high = mid - 1;
            else r = mid; // key found
        }
        r = -(low + 1);  // key not found.
        if (r >= 0) {
        } else {
        }


        enterWrite();
        try {
            int len = elements.length;
            if (count == len) {
                GridAbstractPage<D>[] new_ = new GridAbstractPage[len * 2];
                System.arraycopy(elements, 0, new_, 0, len);
                elements = new_;
            }
            elements[count++] = v;
        } finally {
            exitWrite();
        }
    }

    public List<GridQuery<D>> query(Predicate<String> keyer, Critera<D> filter) {
        enterRead();
        try {
            // locate pages
            List<GridQuery<D>> lst = null;
            for (int i = 0; i < count; i++) {
                GridAbstractPage<D> v = elements[i];
                if (keyer.test(v.id)) {
                    if (lst == null) lst = new ArrayList<>(16);
                    lst.add(v.newQuery(filter));
                }
            }
            if (lst != null) {
                // in parallel fork join
                GridQuery.invokeAll(lst);
            }
            return lst;
        } finally {
            exitRead();
        }
    }

}
