package greatbone.framework.grid;

import greatbone.framework.util.SpinWait;

import java.util.ArrayList;
import java.util.List;

/**
 * element pages that are sorted in the order of page ID
 */
class GridPageList<K, D extends GridData<D>> extends SpinWait {

    // all element pages that are sorted in the order of page ID
    GridPage<K, D>[] elements;
    int count;

    @SuppressWarnings("unchecked")
    GridPageList(int cap) {
        elements = new GridPage[cap];
    }

    void insert(GridPage<K, D> v) {

        GridPage<K, D>[] array = elements;

        int low = 0;
        int high = count - 1;

        int r = 0;
        while (low <= high) {
            int mid = (low + high) >>> 1;
            GridPage<K, D> midv = array[mid];
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
                GridPage<K, D>[] new_ = new GridPage[len * 2];
                System.arraycopy(elements, 0, new_, 0, len);
                elements = new_;
            }
            elements[count++] = v;
        } finally {
            exitWrite();
        }
    }

    public List<GridQuery<K, D>> query(Critera<K> keyer, Critera<D> filter) {
        enterRead();
        try {
            // locate pages
            List<GridQuery<K, D>> lst = null;
            for (int i = 0; i < count; i++) {
                GridPage<K, D> v = elements[i];
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
