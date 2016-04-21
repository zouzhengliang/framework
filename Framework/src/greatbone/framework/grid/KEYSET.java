package greatbone.framework.grid;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * string array
 */
public class KEYSET extends GridColumn {

    // number of characters for each element
    int chars;

    // max number of elements
    int count;

    public KEYSET(int chars, int count) {
        this.chars = chars;
        this.count = count;
    }

    @Override
    public int size() {
        return count * chars;
    }

    String getValueAsString(GridData dat) {
        return null;
    }

    public String[] getValue(GridData dat) {
        return null;
    }

    public int tryValue(GridData dat, String v) {
        return -1;
    }

    public void putValue(GridData dat, String[] v) {
    }

    void putValueAsString(GridData dat, String v) {
    }

    @Override
    void load(GridData dat, ResultSet rs) throws SQLException {
        putValueAsString(dat, rs.getString(ordinal));
    }

    @Override
    void param(GridData dat, PreparedStatement pstmt) throws SQLException {
        pstmt.setString(ordinal, getValueAsString(dat));
    }

}

