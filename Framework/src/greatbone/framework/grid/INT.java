package greatbone.framework.grid;

/**
 */
public class INT extends GridColumn<Integer> {

    public int get(GridData dat) {
        return dat.getInt(offset);
    }

    public void put(GridData dat, int v) {
        dat.putInt(offset, v);
    }

    final int size() {
        return 4;
    }

}
