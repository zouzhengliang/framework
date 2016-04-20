package greatbone.framework.grid;

import greatbone.framework.Decimal;

import java.math.BigDecimal;

/**
 */
public class DECIMAL extends GridColumn<Decimal> {

    int precision;

    public DECIMAL(int precision) {
        this.precision = precision;
    }

    public Decimal get(GridData dat) {
        return null;
    }

    public void put(GridData dat, Decimal v) {

    }

    public int compare(GridData dat, Decimal v) {
        return -1;
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
