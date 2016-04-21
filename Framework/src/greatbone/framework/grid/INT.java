package greatbone.framework.grid;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 */
public class INT extends GridColumn<Integer> {

    public int getValue(GridData dat) {
        return dat.getInt(offset);
    }

    public void putValue(GridData dat, int v) {
        dat.putInt(offset, v);
    }

    final int size() {
        return 4;
    }

    @Override
    String dbtype() {
        return "INT";
    }

    @Override
    void load(GridData dat, ResultSet rs) throws SQLException {
        putValue(dat, rs.getInt(ordinal));
    }

    @Override
    void param(GridData dat, PreparedStatement pstmt) throws SQLException {
        pstmt.setInt(ordinal, getValue(dat));
    }

}
