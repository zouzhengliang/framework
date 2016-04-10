package greatbone.framework.grid;

import java.math.BigDecimal;

/**
 */
public class MONEY extends GridColumn<BigDecimal> {

    public int getValue(GridData dat) {
        return 0;
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
