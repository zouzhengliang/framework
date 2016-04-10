package greatbone.framework.grid;

/**
 */
public class BINARY extends GridColumn<byte[]> {

    final int length;

    public BINARY(byte kbytes) {
        this.length = kbytes * 1024;
    }

    @Override
    public int size() {
        return length;
    }

}
