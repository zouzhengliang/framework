package greatbone.framework.grid;

import greatbone.framework.util.SpinWait;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * A collection of data pages that are basically in addition order.
 */
public class GridPages<D extends GridData<D>> {

    final SpinWait sync = new SpinWait();

    // all element pages
    GridPage<D>[] elements;

    // actual number of elements
    int size;

    @SuppressWarnings("unchecked")
    GridPages(int cap) {
        elements = new GridPage[cap];
    }

    public GridPage<D> get(int index) {
        sync.enterRead();
        try {
            return elements[index];
        } finally {
            sync.exitRead();
        }
    }

    public GridPage<D> get(String pageid) {
        sync.enterRead();
        try {
            for (int i = 0; i < size; i++) {
                GridPage<D> page = elements[i];
                if ((pageid == null)) { // equals
                    if (page.id == null) {
                        return page;
                    }
                } else if (pageid.equals(page.id)) {
                    return page;
                }
            }
            return null;
        } finally {
            sync.exitRead();
        }
    }

    GridPage<D> locate(String datakey) {
        if (datakey != null) {
            sync.enterRead();
            try {
                for (int i = 0; i < size; i++) {
                    GridPage<D> page = elements[i];
                    if (datakey.startsWith(page.id)) { // starts with
                        return page;
                    }
                }
            } finally {
                sync.exitRead();
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    void add(GridPage<D> v) {
        sync.enterWrite();
        try {
            int len = elements.length;
            if (size == len) {
                GridPage<D>[] new_ = new GridPage[len * 2];
                System.arraycopy(elements, 0, new_, 0, len);
                elements = new_;
            }
            elements[size++] = v;
        } finally {
            sync.exitWrite();
        }
    }

    D search(String pageid, Critera<D> filter) {
        GridSearch<D> search = null;
        for (int i = 0; i < size; i++) {
            GridPage<D> v = elements[i];
            if (v.id.equals(pageid)) {
                search = new GridSearch<>(v, filter, false);
            }
        }
        if (search != null) {
            GridSearch.invokeAll(search);
            return search.result;
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    D[] search(Predicate<String> locator, Critera<D> filter) {
        sync.enterRead();
        try {
            // locate pages
            List<GridSearch<D>> lst = null;
            for (int i = 0; i < size; i++) {
                GridPage<D> v = elements[i];
                if (locator.test(v.id)) {
                    if (lst == null) lst = new ArrayList<>(16);
                    lst.add(new GridSearch<>(v, filter, false));
                }
            }
            if (lst != null) {
                // in parallel fork join
                GridSearch.invokeAll(lst);
                int len = lst.size();
                D[] ret = (D[]) new GridData[len];
                for (int i = 0; i < len; i++) {
                    ret[i] = lst.get(i).result;
                }
                return ret;
            }
            return null;
        } finally {
            sync.exitRead();
        }
    }

    public D[] walk(Critera<D> filter, boolean ascending) {
        return null;
    }

    public D[] search(Critera<D> filter, boolean ascending) {
        List<GridSearch<D>> tasks = null;
        for (int i = 0; i < size; i++) {
            GridPage<D> page = elements[i];
            if (tasks == null) tasks = new ArrayList<>(16);
            tasks.add(new GridSearch<>(page, filter, ascending));
        }
        // in parallel fork join
//            GridSearch.invokeAll(this);
//            int len = lst.size();
//            D[] ret = (D[]) new GridData[len];
//            for (int i = 0; i < len; i++) {
//                ret[i] = lst.get(i).result;
//            }
//            return ret;
        return null;
    }


}
