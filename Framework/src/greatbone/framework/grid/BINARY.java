package greatbone.framework.grid;

/**
 */
public class BINARY extends GridColumn<byte[]> {

    final int length;

    public BINARY(byte kbytes) {
        this.length = kbytes * 1024;
    }

    public byte[] get(GridData dat) {
//        return dat.getString(offset);
        return null;
    }

    public void put(GridData dat, byte[] v) {
//        dat.putString(offset, v, len);
    }


    @Override
    public int size() {
        return length;
    }

}
