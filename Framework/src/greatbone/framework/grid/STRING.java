package greatbone.framework.grid;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * A character string encoded in UTF-16.
 */
public class STRING extends GridColumn<String> {

    // maximun number of characters
    final int length;

    public STRING(int length) {
        this.length = length;
    }

    @Override
    public int size() {
        return length * 2;
    }

    @Override
    String dbtype() {
        return "VARCHAR(" + size() + ")";
    }

    public int tryValue(GridData dat, String v) {
        return -1;
    }

    public String getValue(GridData dat) {
        return dat.getString(offset, length);
    }

    public void putValue(GridData dat, String v) {
        dat.putString(offset, v, length);
    }

    @Override
    void load(GridData dat, ResultSet rs) throws SQLException {
        putValue(dat, rs.getString(ordinal));
    }

    @Override
    void param(GridData dat, PreparedStatement pstmt) throws SQLException {
        pstmt.setString(ordinal, getValue(dat));
    }

}
