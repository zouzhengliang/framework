package greatbone.framework.grid;

import greatbone.framework.util.SpinWait;

/**
 * A collection of element pages ordered by ID.
 */
class GridFolderLot extends SpinWait {

    // all element pages
    GridFolder[] elements;
    int count;

    @SuppressWarnings("unchecked")
    GridFolderLot(int cap) {
        elements = new GridFolder[cap];
    }

    void insert(GridFolder v) {

        GridFolder[] elems = elements;

        int low = 0;
        int high = count - 1;

        int r = 0;
        while (low <= high) {
            int mid = (low + high) >>> 1;
            GridFolder midv = elems[mid];
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
                GridFolder[] new_ = new GridFolder[len * 2];
                System.arraycopy(elements, 0, new_, 0, len);
                elements = new_;
            }
            elements[count++] = v;
        } finally {
            exitWrite();
        }
    }

}
