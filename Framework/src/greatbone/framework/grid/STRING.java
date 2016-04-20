package greatbone.framework.grid;

/**
 * A character string encoded in UTF-16.
 */
public class STRING extends GridColumn<String> {

    // maximun number of characters
    final int length;

    public STRING(int length) {
        this.length = length;
    }

    public int tryValue(GridData dat, String v) {
        return -1;
    }

    public String getValue(GridData dat) {
        return dat.getString(offset, length);
    }

    public void putValue(GridData dat, String v) {
        dat.putString(offset, v, length);
    }

    @Override
    public int size() {
        return length * 2;
    }

}
