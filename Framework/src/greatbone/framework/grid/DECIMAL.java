package greatbone.framework.grid;

import greatbone.framework.Decimal;

/**
 */
public class DECIMAL extends GridColumn<Decimal> {

    int precision;

    public DECIMAL(int precision) {
        this.precision = precision;
    }

    public int tryValue(GridData dat, Decimal v) {
        return -1;
    }

    public Decimal getValue(GridData dat) {
        return null;
    }

    public void putValue(GridData dat, Decimal v) {

    }

    @Override
    public int size() {
        return 8;
    }

    public int add(GridData data, int a) {
        return 0;
    }

    public int addAndSet(GridData data, int a) {
        return 0;
    }

}
