package greatbone.framework.grid;

/**
 * character string
 */
public class STRING extends GridColumn<String> {

    final int len;

    public STRING(int len) {
        this.len = len;
    }

    public String get(GridData dat) {
        return dat.getString(offset, len);
    }

    public void put(GridData dat, String v) {
        dat.putString(offset, v, len);
    }


    @Override
    public int size() {
        return len * 2; // UTF-16 encoded
    }

    public int compare(GridData data, String v) {
        return -1;
    }

}
