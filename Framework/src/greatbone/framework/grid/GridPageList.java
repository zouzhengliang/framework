package greatbone.framework.grid;

import java.util.AbstractList;

/**
 */
public class GridPageList<D extends GridData<D>> extends AbstractList<GridPage<D>> {

    @Override
    public GridPage<D> get(int index) {
        return null;
    }

    @Override
    public int size() {
        return 0;
    }

    D search(Critera<D> filter) {
        for (int i = 0; i < size(); i++) {

//                add(new GridSearch<>(get(i), filter));
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
