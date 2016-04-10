package greatbone.framework.grid;

/**
 * integer array
 */
public class LONGA extends GridColumn<long[]> {

    int length;

    public LONGA(int len) {
    }

    @Override
    public int size() {
        return length * 8;
    }

}
