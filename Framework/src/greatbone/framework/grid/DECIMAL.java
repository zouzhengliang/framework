package greatbone.framework.grid;

import greatbone.framework.Decimal;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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

    public void putValue(GridData dat, BigDecimal v) {

    }

    @Override
    public int size() {
        return 8;
    }

    @Override
    void load(GridData dat, ResultSet rs) throws SQLException {
        putValue(dat, rs.getBigDecimal(ordinal));
    }

    public int add(GridData data, int a) {
        return 0;
    }

    public int addAndSet(GridData data, int a) {
        return 0;
    }

    @Override
    void param(GridData dat, PreparedStatement pstmt) throws SQLException {

    }

}
