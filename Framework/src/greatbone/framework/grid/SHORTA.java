package greatbone.framework.grid;

/**
 * integer array
 */
public class SHORTA extends GridColumn<short[]> {

    int length;

    public SHORTA(int len) {
    }

    @Override
    public int size() {
        return length * 8;
    }

}
