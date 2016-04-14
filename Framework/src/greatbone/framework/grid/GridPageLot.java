package greatbone.framework.grid;

import greatbone.framework.util.SpinWait;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * A collection of element pages ordered by ID.
 */
class GridPageLot<D extends GridData<D>> extends SpinWait {

    // all element pages
    GridPage<D>[] elements;
    int count;

    @SuppressWarnings("unchecked")
    GridPageLot(int cap) {
        elements = new GridPage[cap];
    }

    void insert(GridPage<D> v) {

        GridPage<D>[] elems = elements;

        int low = 0;
        int high = count - 1;

        int r = 0;
        while (low <= high) {
            int mid = (low + high) >>> 1;
            GridPage<D> midv = elems[mid];
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
                GridPage<D>[] new_ = new GridPage[len * 2];
                System.arraycopy(elements, 0, new_, 0, len);
                elements = new_;
            }
            elements[count++] = v;
        } finally {
            exitWrite();
        }
    }

    @SuppressWarnings("unchecked")
    D[] query(Predicate<String> locator, Critera<D> filter) {
        enterRead();
        try {
            // locate pages
            List<GridQuery<D>> lst = null;
            for (int i = 0; i < count; i++) {
                GridPage<D> v = elements[i];
                if (locator.test(v.id)) {
                    if (lst == null) lst = new ArrayList<>(16);
                    lst.add(v.newQuery(filter));
                }
            }
            if (lst != null) {
                // in parallel fork join
                GridQuery.invokeAll(lst);
                int len = lst.size();
                D[] ret = (D[]) new GridData[len];
                for (int i = 0; i < len; i++) {
                    ret[i] = lst.get(i).result;
                }
                return ret;
            }
            return null;
        } finally {
            exitRead();
        }
    }

}
