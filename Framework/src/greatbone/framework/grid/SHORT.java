package greatbone.framework.grid;

/**
 */
public class SHORT extends GridColumn<Short> {

    public short get(GridData data) {
        return data.getShort(offset);
    }

    public void put(GridData data, short v) {
        data.putShort(offset, v);
    }

    @Override
    public final int size() {
        return 2;
    }

}
