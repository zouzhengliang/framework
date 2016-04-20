package greatbone.framework.grid;

/**
 */
public class SHORT extends GridColumn<Short> {

    public int tryValue(GridData dat, short v) {
        return -1;
    }

    public short getValue(GridData dat) {
        return dat.getShort(offset);
    }

    public void putValue(GridData dat, short v) {
        dat.putShort(offset, v);
    }

    @Override
    public final int size() {
        return 2;
    }

}
