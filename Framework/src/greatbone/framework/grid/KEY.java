package greatbone.framework.grid;

/**
 * A character string encoded in ASCII
 */
public class KEY extends GridColumn<String> {

    // maxinum number of characters
    final int length;

    public KEY(int length) {
        this.length = length;
    }

    public String getValue(GridData dat) {
        return dat.getAscii(offset, length);
    }

    public void putValue(GridData dat, String v) {
        dat.putAscii(offset, v, length);
    }

    public int tryValue(GridData dat, String v) {
        return -1;
    }

    @Override
    int size() {
        return length;
    }

}
