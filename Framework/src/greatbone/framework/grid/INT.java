package greatbone.framework.grid;

/**
 */
public class INT extends GridColumn<Integer> {

    public int get(GridData data) {
        return data.getInt(offset);
    }

    public void put(GridData data, int v) {
        data.putInt(offset, v);
    }

    public final int size() {
        return 4;
    }

}
