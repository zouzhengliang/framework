package greatbone.framework.grid;

/**
 * character string
 */
public class STRING extends GridColumn<String> {

    int length;

    public STRING(int len) {
        this.length = len;
    }

    public String get(GridData data) {
        return data.getString(offset);
    }

    public void put(GridData data, String v) {
        data.putString(offset, v);
    }


    @Override
    public int size() {
        return length * 2; // UTF-16 encoded
    }

    public int compare(GridData data, String v) {
        return -1;
    }

}
