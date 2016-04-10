package greatbone.framework.grid;

/**
 * integer array
 */
public class INTA extends GridColumn<int[]> {

    int length;

    public INTA(int len) {

    }

    @Override
    public int size() {
        return length * 4;
    }

}
