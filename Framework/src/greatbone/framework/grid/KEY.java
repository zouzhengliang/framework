package greatbone.framework.grid;

/**
 * A KEY represents a key column of ASCII string type.
 */
class KEY extends GridColumn<String> {

    final int len;

    public KEY(int len) {
        this.len = len;
    }

    public String get(GridData dat) {
        return dat.getAscii(offset, len);
    }

    public void put(GridData dat, String v) {
        dat.putAscii(offset, v, len);
    }

    @Override
    int size() {
        return len;
    }

    int compare(GridData data, String v) {
        return -1;
    }

}
