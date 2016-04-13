package greatbone.framework.grid;

/**
 * KEY represents a key string column.
 */
class KEY extends GridColumn<String> {

    int length;

    public KEY(int len) {
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
